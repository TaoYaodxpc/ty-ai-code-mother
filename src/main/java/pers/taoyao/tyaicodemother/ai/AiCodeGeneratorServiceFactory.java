package pers.taoyao.tyaicodemother.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI代码生成服务工厂配置类
 * 
 * 该类负责创建和配置AI代码生成服务实例。
 * 使用LangChain4j框架集成聊天模型，支持流式和非流式对话模式。
 */
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
    private StreamingChatModel streamingChatModel;

    /**
     * 创建AI代码生成服务Bean
     * 
     * 通过AiServices构建器模式创建服务实例，同时配置
     * 标准聊天模型和流式聊天模型，使服务具备两种交互能力
     * 
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
