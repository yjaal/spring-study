package win.iot4yj.service.impl;

import win.iot4yj.entity.QuartzJob;
import win.iot4yj.mapper.QuartzJobMapper;
import win.iot4yj.service.QuartzJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@Service
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

}
