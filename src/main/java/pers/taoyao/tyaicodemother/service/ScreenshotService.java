package pers.taoyao.tyaicodemother.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 * @date 2026/1/1
 * @Version v1.0
 * @description
 */
public interface ScreenshotService {

    /**
     * 生成网页截图并上传
     *
     * @param webUrl 网页 URL
     * @return 截图文件路径，失败返回 null
     */
    String generateAndUploadScreenshot(String webUrl);
}
