package pers.taoyao.tyaicodemother.langgraph4j.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pers.taoyao.tyaicodemother.langgraph4j.model.ImageCategoryEnum;
import pers.taoyao.tyaicodemother.langgraph4j.model.ImageResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author admin
 * @date 2026/1/19
 * @Version v1.0
 * @description
 */
@SpringBootTest
class UndrawIllustrationToolTest {

    @Resource
    private UndrawIllustrationTool undrawIllustrationTool;

    @Test
    void testSearchIllustrations() {
        // 测试正常搜索插画
        List<ImageResource> illustrations = undrawIllustrationTool.searchIllustrations("happy");
        assertNotNull(illustrations);
        // 验证返回的插画资源
        ImageResource firstIllustration = illustrations.get(0);
        assertEquals(ImageCategoryEnum.ILLUSTRATION, firstIllustration.getCategory());
        assertNotNull(firstIllustration.getDescription());
        assertNotNull(firstIllustration.getUrl());
        assertTrue(firstIllustration.getUrl().startsWith("http"));
        System.out.println("搜索到 " + illustrations.size() + " 张插画");
        illustrations.forEach(illustration ->
                System.out.println("插画: " + illustration.getDescription() + " - " + illustration.getUrl())
        );
    }
}
