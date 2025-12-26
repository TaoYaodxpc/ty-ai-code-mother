package pers.taoyao.tyaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import pers.taoyao.tyaicodemother.model.dto.app.AppQueryRequest;
import pers.taoyao.tyaicodemother.model.entity.App;
import pers.taoyao.tyaicodemother.model.vo.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/TaoYaodxpc">TaoYao</a>
 */
public interface AppService extends IService<App> {

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
}
