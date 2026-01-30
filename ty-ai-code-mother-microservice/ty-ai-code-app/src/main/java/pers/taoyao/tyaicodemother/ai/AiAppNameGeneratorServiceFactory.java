package pers.taoyao.tyaicodemother.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import pers.taoyao.tyaicodemother.utils.SpringContextUtil;

/**
 * AI代码生成服务工厂配置类
 * 
 * 该类负责创建和配置AI代码生成服务实例。
 * 使用LangChain4j框架集成聊天模型，支持流式和非流式对话模式。
 */
@Slf4j
@Configuration
public class AiAppNameGeneratorServiceFactory {

    /**
     * 注入标准聊天模型
     * 用于处理同步请求，等待完整响应后返回结果
     */
    // private ChatModel chatModel;

    public AiAppNameGeneratorService createAiAppNameGeneratorService() {
        ChatModel chatModel = SpringContextUtil.getBean("routingChatModelPrototype", ChatModel.class);
        return AiServices.builder(AiAppNameGeneratorService.class)
                .chatModel(chatModel)
                .build();
    }

    public AiAppNameGeneratorService aiAppNameGeneratorService() {
        return createAiAppNameGeneratorService();
    }
}
