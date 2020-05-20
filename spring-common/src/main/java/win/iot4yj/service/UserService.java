package win.iot4yj.service;

import java.util.List;
import win.iot4yj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author joyang
 * @since 2020-05-18
 */
public interface UserService extends IService<User> {

    List<User> getPage(int num, int count, User user);

}
