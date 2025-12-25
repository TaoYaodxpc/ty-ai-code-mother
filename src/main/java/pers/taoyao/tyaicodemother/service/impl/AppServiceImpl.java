package pers.taoyao.tyaicodemother.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import pers.taoyao.tyaicodemother.model.entity.App;
import pers.taoyao.tyaicodemother.mapper.AppMapper;
import pers.taoyao.tyaicodemother.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://github.com/TaoYaodxpc">TaoYao</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
