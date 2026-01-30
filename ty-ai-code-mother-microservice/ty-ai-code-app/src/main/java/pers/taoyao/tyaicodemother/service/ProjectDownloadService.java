package pers.taoyao.tyaicodemother.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author admin
 * @date 2026/1/1
 * @Version v1.0
 * @description
 */
public interface ProjectDownloadService {

    /**
     * 下载项目为 zip
     *
     * @param projectPath 项目路径
     * @param downloadFileName 下载文件名
     * @param response 响应
     * @return 下载结果
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
