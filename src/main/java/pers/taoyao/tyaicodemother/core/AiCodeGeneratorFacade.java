package pers.taoyao.tyaicodemother.core;

import cn.hutool.json.JSONUtil;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.taoyao.tyaicodemother.ai.AiAppNameGeneratorService;
import pers.taoyao.tyaicodemother.ai.AiAppNameGeneratorServiceFactory;
import pers.taoyao.tyaicodemother.ai.AiCodeGeneratorService;
import pers.taoyao.tyaicodemother.ai.AiCodeGeneratorServiceFactory;
import pers.taoyao.tyaicodemother.ai.model.AppNameResult;
import pers.taoyao.tyaicodemother.ai.model.HtmlCodeResult;
import pers.taoyao.tyaicodemother.ai.model.MultiFileCodeResult;
import pers.taoyao.tyaicodemother.ai.model.enums.CodeGenTypeEnum;
import pers.taoyao.tyaicodemother.ai.model.message.AiResponseMessage;
import pers.taoyao.tyaicodemother.ai.model.message.ToolExecutedMessage;
import pers.taoyao.tyaicodemother.ai.model.message.ToolRequestMessage;
import pers.taoyao.tyaicodemother.constant.AppConstant;
import pers.taoyao.tyaicodemother.core.builder.VueProjectBuilder;
import pers.taoyao.tyaicodemother.core.parser.CodeParserExecutor;
import pers.taoyao.tyaicodemother.core.saver.CodeFileSaverExecutor;
import pers.taoyao.tyaicodemother.exception.BusinessException;
import pers.taoyao.tyaicodemother.exception.ErrorCode;
import pers.taoyao.tyaicodemother.exception.ThrowUtils;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    @Resource
    private AiAppNameGeneratorServiceFactory aiAppNameGeneratorServiceFactory;

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    public String generateAppName(String userMessage) {
        ThrowUtils.throwIf(userMessage == null, ErrorCode.PARAMS_ERROR, "用户提示词不能为空");
        AiAppNameGeneratorService aiAppNameGeneratorService = aiAppNameGeneratorServiceFactory.createAiAppNameGeneratorService();
        AppNameResult appNameResult = aiAppNameGeneratorService.generateAppName(userMessage);
        if (appNameResult == null) {
            log.error("生成应用名称失败");
            return null;
        }
        return appNameResult.getAppName();
    }

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 根据 appId 创建服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, codeGenTypeEnum, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, codeGenTypeEnum, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用 ID
     * @return 处理后的代码流
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 根据 appId 创建服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, userMessage);
                yield processTokenStream(tokenStream, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
     * @param appId       应用 ID
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream, Long appId) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        // 执行 Vue 项目构建（同步执行，确保预览时项目已就绪）
                        String projectDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + "/vue_project_" + appId;
                        vueProjectBuilder.buildProject(projectDirPath);
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }

    /**
     * 通用 处理代码流式返回，并保存代码
     *
     * @param codeStream  代码流
     * @param codeGenType 生成类型
     * @param appId       应用 ID
     * @return 处理后的代码流
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            // 实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            try {
                // 流式返回完成后保存代码
                String completeCode = codeBuilder.toString();
                // 使用执行器解析代码为对象
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                // 使用执行器保存代码到文件
                File codeFile = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                log.info("保存成功，路径为：{}", codeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败: {}", e.getMessage());
            }
        });
    }

}
