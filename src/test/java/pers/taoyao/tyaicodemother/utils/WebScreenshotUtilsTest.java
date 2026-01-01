package pers.taoyao.tyaicodemother.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author admin
 * @date 2026/1/1
 * @Version v1.0
 * @description
 */
class WebScreenshotUtilsTest {

    @Test
    void saveWebPageScreenshot() {
        String path = WebScreenshotUtils.saveWebPageScreenshot("https://www.codefather.com");
        Assertions.assertNotNull(path);
    }
}