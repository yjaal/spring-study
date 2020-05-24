package win.iot4yj.service.impl;

import win.iot4yj.entity.QuartzCron;
import win.iot4yj.mapper.QuartzCronMapper;
import win.iot4yj.service.QuartzCronService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 触发事件表达式 服务实现类
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@Service
public class QuartzCronServiceImpl extends ServiceImpl<QuartzCronMapper, QuartzCron> implements QuartzCronService {

}
