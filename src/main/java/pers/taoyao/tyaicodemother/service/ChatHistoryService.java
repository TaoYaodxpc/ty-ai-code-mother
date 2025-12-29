package pers.taoyao.tyaicodemother.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import pers.taoyao.tyaicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import pers.taoyao.tyaicodemother.model.entity.ChatHistory;
import pers.taoyao.tyaicodemother.model.entity.User;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/TaoYaodxpc">TaoYao</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 添加对话历史
     *
     * @param appId 应用 ID
     * @param message 消息
     * @param messageType 消息类型
     * @param userId 用户 ID
     * @return 是否添加成功
     */
    boolean addChatHistory(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用 ID 删除对话历史
     *
     * @param appId 应用 ID
     * @return 是否删除成功
     */
    boolean deleteByAppId(Long appId);

    /**
     * 根据应用 ID 分页获取对话历史
     *
     * @param appId 应用 ID
     * @param pageSize 页大小
     * @param lastCreateTime 最后创建时间
     * @param loginUser 登录用户
     * @return 对话历史列表
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 加载对话历史到内存
     *
     * @param appId 应用 ID
     * @param chatMemory 对话记忆
     * @param maxCount 最大数量
     * @return 加载数量
     */
    int loadChatHistory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 获取查询条件
     *
     * @param chatHistoryQueryRequest 查询条件
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
