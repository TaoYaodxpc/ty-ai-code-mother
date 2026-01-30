package pers.taoyao.tyaicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 *
 * @author admin
 * @date 2025/12/29
 * @Version v1.0
 * @description
 */
@Description("生成应用名称的结果")
@Data
public class AppNameResult {

    @Description("应用名称")
    private String appName;
}
