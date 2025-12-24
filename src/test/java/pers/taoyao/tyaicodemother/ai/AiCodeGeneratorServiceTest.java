package pers.taoyao.tyaicodemother.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pers.taoyao.tyaicodemother.ai.model.HtmlCodeResult;
import pers.taoyao.tyaicodemother.ai.model.MultiFileCodeResult;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author admin
 * @date 2025/12/20
 * @Version v1.0
 * @description
 */
@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做个程序员鱼皮的工作记录小工具，不超过 20 行");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("做个程序员鱼皮的留言板，不超过 30 行");
        Assertions.assertNotNull(multiFileCode);
    }

}
