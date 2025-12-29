package pers.taoyao.tyaicodemother.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.taoyao.tyaicodemother.service.ChatHistoryService;

import java.time.Duration;

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
    @Resource
    private ChatModel chatModel;

    @Bean
    public AiAppNameGeneratorService aiAppNameGeneratorService() {
        return AiServices.builder(AiAppNameGeneratorService.class)
                .chatModel(chatModel)
                // 根据 appId 创建一个唯一的聊天内存
                .build();
    }
}
