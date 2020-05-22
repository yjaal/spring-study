package win.iot4yj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import win.iot4yj.entity.User;
import win.iot4yj.mapper.UserMapper;
import win.iot4yj.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author joyang
 * @since 2020-05-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;


    @Override
    public List<User> getPage(int num, int count, User user) {
        //默认起始页为1，每页显示10条
        Page<User> page = new Page<>(num, count);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        IPage<User> pageRes = userMapper.selectPage(page, null);
        List<User> users = pageRes.getRecords();
        long size = pageRes.getSize();
        long total = pageRes.getTotal();
        log.info("查询出{}条数据，总共有{}条数据", size, total);
        return users;
    }
}
