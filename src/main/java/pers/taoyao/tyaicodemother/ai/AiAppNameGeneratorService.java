package pers.taoyao.tyaicodemother.ai;

import dev.langchain4j.service.SystemMessage;
import pers.taoyao.tyaicodemother.ai.model.AppNameResult;

/**
 *
 * @author admin
 * @date 2025/12/16
 * @Version v1.0
 * @description
 */
public interface AiAppNameGeneratorService {

    /**
     * 生成 HTML 代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/appNameGen-system-prompt.txt")
    AppNameResult generateAppName(String userMessage);

}

