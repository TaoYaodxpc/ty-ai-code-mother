package pers.taoyao.tyaicodemother.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pers.taoyao.tyaicodemother.service.AppService;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author admin
 * @date 2025/12/27
 * @Version v1.0
 * @description
 */
@SpringBootTest
class AppServiceImplTest {

    @Resource
    private AppService appService;

    @Test
    void genAppName() {
        String appName = appService.genAppName("帮我生成一个任务记录网站", "开心的果子");
        Assertions.assertNotNull(appName);
    }
}