package pers.taoyao.tyaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import pers.taoyao.tyaicodemother.model.dto.app.AppAddRequest;
import pers.taoyao.tyaicodemother.model.dto.app.AppQueryRequest;
import pers.taoyao.tyaicodemother.model.entity.App;
import pers.taoyao.tyaicodemother.model.entity.User;
import pers.taoyao.tyaicodemother.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/TaoYaodxpc">TaoYao</a>
 */
public interface AppService extends IService<App> {

    /**
     * 创建应用
     *
     * @param appAddRequest 应用添加请求
     * @param loginUser 登录用户
     * @return 应用 ID
     */
    Long createApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * 通过对话生成应用代码
     *
     * @param appId 应用 ID
     * @param message 消息
     * @param loginUser 登录用户
     * @return 生成的代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 部署应用
     *
     * @param appId 应用 ID
     * @param loginUser 登录用户
     * @return 部署结果（可访问的部署地址）
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 异步生成应用截图
     *
     * @param appId 应用 ID
     * @param appDeployUrl 应用 URL
     */
    void generateAppScreenshotAsync(Long appId, String appDeployUrl);

    /**
     * 获取应用视图对象
     *
     * @param app 应用实体对象
     * @return 应用视图对象
     */
    AppVO getAppVO(App app);

    /**
     * 获取查询条件
     *
     * @param appQueryRequest 应用查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 获取应用视图对象列表
     *
     * @param appList 应用实体对象列表
     * @return 应用视图对象列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 生成应用名称
     *
     * @param initPrompt 初始提示
     * @param userName 登录用户名
     */
    String genAppName(String initPrompt, String userName);
}
