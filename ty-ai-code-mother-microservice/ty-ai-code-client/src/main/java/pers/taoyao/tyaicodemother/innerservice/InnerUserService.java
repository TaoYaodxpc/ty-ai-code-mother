package pers.taoyao.tyaicodemother.innerservice;

import jakarta.servlet.http.HttpServletRequest;
import pers.taoyao.tyaicodemother.exception.BusinessException;
import pers.taoyao.tyaicodemother.exception.ErrorCode;
import pers.taoyao.tyaicodemother.model.entity.User;
import pers.taoyao.tyaicodemother.model.vo.UserVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static pers.taoyao.tyaicodemother.constant.UserConstant.USER_LOGIN_STATE;

/**
 *
 * @author admin
 * @date 2026/1/30
 * @Version v1.0
 * @description 内部使用的用户服务，其它服务需要用到的方法（服务）
 */
public interface InnerUserService {

    List<User> listByIds(Collection<? extends Serializable> ids);

    User getById(Serializable id);

    UserVO getUserVO(User user);

    // 静态方法，避免跨服务调用 （其实就是调用 SDK，比如 hutool 工具类）
    static User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }
}

