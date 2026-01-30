package pers.taoyao.tyaicodemother.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import pers.taoyao.tyaicodemother.innerservice.InnerScreenshotService;
import pers.taoyao.tyaicodemother.service.ScreenshotService;

/**
 * 内部截图服务实现类
 */
@DubboService
public class InnerScreenshotServiceImpl implements InnerScreenshotService {

    @Resource
    private ScreenshotService screenshotService;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        return screenshotService.generateAndUploadScreenshot(webUrl);
    }
}
