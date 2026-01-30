package pers.taoyao.tyaicodemother.core.builder;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author admin
 * @date 2025/12/30
 * @Version v1.0
 * @description Vue 项目构建器
 */
@Slf4j
@Component
public class VueProjectBuilder {

    /**
     * 异步构建 Vue 项目
     *
     * @param projectDirPath 项目目录路径
     */
    public void buildProjectAsync(String projectDirPath) {
        Thread.ofVirtual().name("vue-builder-" + System.currentTimeMillis())
                .start(() -> {
                    try {
                        buildProject(projectDirPath);
                    } catch (Exception e) {
                        log.error("异步构建 Vue 项目时发生异常: {}", e.getMessage(), e);
                    }
                });
    }

    /**
     * 构建 Vue 项目
     *
     * @param projectDirPath 项目目录路径
     * @return 是否构建成功
     */
    public boolean buildProject(String projectDirPath) {
        File projectDir = new File(projectDirPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            log.error("项目目录不存在或无效: {}", projectDirPath);
            return false;
        }
        // 检查是否有 package.json 文件
        File packageJsonFile = new File(projectDir, "package.json");
        if (!packageJsonFile.exists()) {
            log.error("项目目录下不存在 package.json 文件: {}", projectDirPath);
            return false;
        }
        log.info("开始构建 Vue 项目: {}", projectDirPath);
        // 执行 npm install 命令
        if (!executeNpmInstall(projectDir)) {
            log.error("npm install 执行失败");
            return false;
        }
        // 执行 npm run build 命令
        if (!executeNpmBuild(projectDir)) {
            log.error("npm run build 执行失败");
            return false;
        }
        // 验证 dist 目录是否生成
        File distDir = new File(projectDir, "dist");
        if (!distDir.exists() || !distDir.isDirectory()) {
            log.error("构建完成，但是项目目录下不存在 dist 目录: {}", projectDirPath);
            return false;
        }
        log.info("Vue 项目构建成功，dist 目录: {}", projectDirPath);
        return true;
    }

    /**
     * 执行 npm install 命令
     */
    private boolean executeNpmInstall(File projectDir) {
        log.info("执行 npm install...");
        String command = String.format("%s install", buildCommand("npm"));
        return executeCommand(projectDir, command, 300); // 5分钟超时
    }

    /**
     * 执行 npm run build 命令
     */
    private boolean executeNpmBuild(File projectDir) {
        log.info("执行 npm run build...");
        String command = String.format("%s run build", buildCommand("npm"));
        return executeCommand(projectDir, command, 180); // 3分钟超时
    }

    /**
     * 判断当前操作系统是否为 Windows
     *
     * @return true 表示是 Windows，false 表示不是 Windows
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 根据操作系统构建命令
     *
     * @param baseCommand 命令基础部分
     * @return 完整的命令字符串
     */
    private String buildCommand(String baseCommand) {
        if (isWindows()) {
            return baseCommand + ".cmd";
        }
        return baseCommand;
    }

    /**
     * 执行命令
     *
     * @param workingDir     工作目录
     * @param command        命令字符串
     * @param timeoutSeconds 超时时间（秒）
     * @return 是否执行成功
     */
    private boolean executeCommand(File workingDir, String command, int timeoutSeconds) {
        try {
            log.info("在目录 {} 中执行命令: {}", workingDir.getAbsolutePath(), command);
            Process process = RuntimeUtil.exec(
                    null,
                    workingDir,
                    command.split("\\s+") // 命令分割为数组
            );
            // 等待进程完成，设置超时
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                log.error("命令执行超时（{}秒），强制终止进程", timeoutSeconds);
                process.destroyForcibly();
                return false;
            }
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("命令执行成功: {}", command);
                return true;
            } else {
                log.error("命令执行失败，退出码: {}", exitCode);
                return false;
            }
        } catch (Exception e) {
            log.error("执行命令失败: {}, 错误信息: {}", command, e.getMessage());
            return false;
        }
    }

}
