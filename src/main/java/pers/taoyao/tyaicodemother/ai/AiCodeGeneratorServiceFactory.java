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

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<Long, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，appId: {}, 原因: {}", key, cause);
            })
            .build();

    /**
     * 创建AI代码生成服务实例
     *
     * 根据 appId 创建一个独立的对话记忆，并使用 AiServices 创建服务实例
     *
     * @param appId 应用 ID
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return serviceCache.get(appId, this::createAiCodeGeneratorService);
    }

    /**
     * 创建 AI 代码生成服务实例
     *
     * @param appId 应用 ID
     * @return 配置完成的 AiCodeGeneratorService 实例
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId) {
        log.info("创建 AI 服务实例，appId: {}", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        // 从数据库中加载对话历史到记忆中
        chatHistoryService.loadChatHistory(appId, chatMemory, 20);
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                // 根据 appId 创建一个唯一的聊天内存
                .chatMemory(chatMemory)
                .build();
    }

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
        return getAiCodeGeneratorService(0L);
    }
}
