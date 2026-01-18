package pers.taoyao.tyaicodemother.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.taoyao.tyaicodemother.ai.model.enums.CodeGenTypeEnum;
import pers.taoyao.tyaicodemother.ai.tools.*;
import pers.taoyao.tyaicodemother.exception.BusinessException;
import pers.taoyao.tyaicodemother.exception.ErrorCode;
import pers.taoyao.tyaicodemother.service.ChatHistoryService;

import java.time.Duration;

/**
 * AI代码生成服务工厂配置类
 * <p>
 * 该类负责创建和配置AI代码生成服务实例。
 * 使用LangChain4j框架集成聊天模型，支持流式和非流式对话模式。
 */
@Slf4j
@Configuration
public class AiCodeGeneratorServiceFactory {

    /**
     * 注入标准聊天模型
     * 用于处理同步请求，等待完整响应后返回结果
     */
    @Resource
    private ChatModel chatModel;

    /**
     * 注入流式聊天模型
     * 用于处理异步请求，实时返回响应内容
     */
    @Resource
    private StreamingChatModel openAiStreamingChatModel;

    @Resource
    private StreamingChatModel reasoningStreamingChatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private ToolManager toolManager;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，缓存键: {}, 原因: {}", key, cause);
            })
            .build();

    /**
     * 创建 AI 代码生成服务实例（为了兼容老逻辑）
     * <p>
     * 根据 appId 创建一个独立的对话记忆，并使用 AiServices 创建服务实例
     *
     * @param appId 应用 ID
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return getAiCodeGeneratorService(appId, CodeGenTypeEnum.HTML);
    }

    /**
     * 创建 AI 代码生成服务实例
     * <p>
     * 根据 appId 创建一个独立的对话记忆，并使用 AiServices 创建服务实例
     *
     * @param appId 应用 ID
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        String cacheKey = buildCacheKey(appId, codeGenType);
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenType));
    }

    /**
     * 创建 AI 代码生成服务实例
     *
     * @param appId       应用 ID
     * @param codeGenType 代码生成类型
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        log.info("创建 AI 服务实例，appId: {}", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                // 选择合适的 对话记忆大小
                .maxMessages(100)
                .build();
        // 从数据库中加载对话历史到记忆中
        chatHistoryService.loadChatHistory(appId, chatMemory, 20);
        return switch (codeGenType) {
            // Vue 项目生成，使用工具调用和推理模型
            case VUE_PROJECT -> AiServices.builder(AiCodeGeneratorService.class)
                    .streamingChatModel(reasoningStreamingChatModel)
                    .chatMemoryProvider(memoryId -> chatMemory)
                    .tools(toolManager.getAllTools())
                    .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                            toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()
                    ))
                    .build();
            // HTML 和 多文件生成，使用流式对话模型
            case HTML, MULTI_FILE -> AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(openAiStreamingChatModel)
                    // 根据 appId 创建一个唯一的聊天内存
                    .chatMemory(chatMemory)
                    .build();
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType.getValue());
        };
    }

    /**
     * 创建 AI 代码生成服务 Bean
     * <p>
     * 通过 AiServices 构建器模式创建服务实例，同时配置
     * 标准聊天模型和流式聊天模型，使服务具备两种交互能力
     *
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return getAiCodeGeneratorService(0L);
    }

    /**
     * 构建缓存键
     *
     * @param appId       应用 ID
     * @param codeGenType 代码生成类型
     * @return 缓存键
     */
    private String buildCacheKey(long appId, CodeGenTypeEnum codeGenType) {
        return appId + "_" + codeGenType.getValue();
    }
}
