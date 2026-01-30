package pers.taoyao.tyaicodeuser.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import pers.taoyao.tyaicodemother.innerservice.InnerUserService;
import pers.taoyao.tyaicodemother.model.entity.User;
import pers.taoyao.tyaicodemother.model.vo.UserVO;
import pers.taoyao.tyaicodeuser.service.UserService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author admin
 * @date 2026/1/30
 * @Version v1.0
 * @description 内部服务实现类
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public List<User> listByIds(Collection<? extends Serializable> ids) {
        return userService.listByIds(ids);
    }

    @Override
    public User getById(Serializable id) {
        return userService.getById(id);
    }

    @Override
    public UserVO getUserVO(User user) {
        return userService.getUserVO(user);
    }
}

