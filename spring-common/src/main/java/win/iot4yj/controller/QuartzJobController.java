package win.iot4yj.controller;


import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import win.iot4yj.dto.JobTrigger;

/**
 * @author joyang
 * 这里写了一个基本的动态添加定时任务的controller，不是很完善，其实可以将quartz中的表全部反向生成过来。
 * 然后统一管理。后面再深入研究
 */
@RestController
@RequestMapping("/job")
@Api(value = "/job", tags = "QuartzJobController")
public class QuartzJobController {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobController.class);

    @Autowired
    private Scheduler scheduler;

    @ApiOperation(value = "添加定时任务", notes = "添加定时任务")
    @PostMapping(value = "/add")
    public String add(@RequestBody JobTrigger jobTrigger) throws Exception {
        Assert.notNull(ApiErrorCode.FAILED, jobTrigger.getJobName());
        //class name必须是完整名
        Assert.notNull(ApiErrorCode.FAILED, jobTrigger.getJobClassName());
        Assert.notNull(ApiErrorCode.FAILED, jobTrigger.getCronExp());
        if (Objects.equals(null, jobTrigger.getJobGroup())) {
            jobTrigger.setJobGroup(Scheduler.DEFAULT_GROUP);
        }
        if (Objects.equals(null, jobTrigger.getTriggerName())) {
            jobTrigger.setTriggerName(jobTrigger.getJobName());
        }
        if (Objects.equals(null, jobTrigger.getTriggerGroup())) {
            jobTrigger.setTriggerGroup(jobTrigger.getJobGroup());
        }
        this.addJob(jobTrigger);
        return "定时任务添加成功";
    }


    private void addJob(JobTrigger jobTrigger) throws Exception {
        // 启动调度器
        scheduler.start();
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobTrigger.getJobClassName()).getClass())
                                        .withIdentity(jobTrigger.getJobName(), jobTrigger.getJobGroup()).build();
        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobTrigger.getCronExp());
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobTrigger.getTriggerName(), jobTrigger.getTriggerGroup())
                                            .withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("创建定时任务失败", e);
            throw new RuntimeException("创建定时任务失败");
        }
    }

    @ApiOperation(value = "根据条件查询定时任务", notes = "根据条件查询定时任务")
    @PostMapping(value = "/query")
    public List<JobTrigger> query(@RequestParam(value = "jobClassName") String jobClassName,
        @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {

        return null;
    }

    @ApiOperation(value = "根据条件暂停定时任务", notes = "根据条件暂停定时任务")
    @PostMapping(value = "/pause")
    public String pause(@RequestParam(value = "jobClassName") String jobClassName,
        @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        this.pauseJob(jobClassName, jobGroupName);
        return "定时任务暂停成功";
    }

    private void pauseJob(String jobClassName, String jobGroupName) throws Exception {
        if (StringUtils.isBlank(jobGroupName)) {
            jobGroupName = Scheduler.DEFAULT_GROUP;
        }
        // 通过SchedulerFactory获取一个调度器实例
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    @ApiOperation(value = "根据条件恢复定时任务", notes = "根据条件恢复定时任务")
    @PostMapping(value = "/resume")
    public String resume(@RequestParam(value = "jobClassName") String jobClassName,
        @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        this.resumeJob(jobClassName, jobGroupName);
        return "定时任务恢复成功";
    }

    public void resumeJob(String jobClassName, String jobGroupName) throws Exception {
        if (StringUtils.isBlank(jobGroupName)) {
            jobGroupName = Scheduler.DEFAULT_GROUP;
        }
        // 通过SchedulerFactory获取一个调度器实例
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    @ApiOperation(value = "根据条件重新调度定时任务", notes = "根据条件重新调度定时任务")
    @PostMapping(value = "/reschedule")
    public String reschedule(@RequestParam(value = "jobClassName") String jobClassName,
        @RequestParam(value = "jobGroupName") String jobGroupName,
        @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        this.rescheduleJob(jobClassName, jobGroupName, cronExpression);

        return "定时任务重新调度成功";
    }

    private void rescheduleJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        if (StringUtils.isBlank(jobGroupName)) {
            jobGroupName = Scheduler.DEFAULT_GROUP;
        }

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            logger.error("更新定时任务失败", e);
            throw new RuntimeException("更新定时任务失败");
        }
    }

    @ApiOperation(value = "根据条件删除定时任务", notes = "根据条件删除定时任务")
    @PostMapping(value = "/delete")
    public String delete(@RequestParam(value = "jobClassName") String jobClassName,
        @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        deleteJob(jobClassName, jobGroupName);
        return "定时任务删除成功";
    }

    private void deleteJob(String jobClassName, String jobGroupName) throws Exception {
        if (StringUtils.isBlank(jobGroupName)) {
            jobGroupName = Scheduler.DEFAULT_GROUP;
        }

        // 通过SchedulerFactory获取一个调度器实例
        //停止触发器
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        //移除触发器
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        //删除任务
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    /**
     * 定时任务管理
     */
    @ApiOperation(value = "定时任务管理", notes = "根据条件管理定时任务")
    @GetMapping(value = "/query")
    public Map<String, Object> query(@RequestParam(value = "pageNum") Integer pageNum,
        @RequestParam(value = "pageSize") Integer pageSize) {
        return null;
    }

    /**
     * 查询定时任务
     */
    @ApiOperation(value = "查询定时任务", notes = "根据条件查询定时任务")
    @GetMapping(value = "/queryJobList")
    public JobTrigger queryJobList(@RequestParam Map<String, Object> paramMap) {
        return null;
    }

    public static QuartzJobBean getClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return (QuartzJobBean) clazz.newInstance();
    }
}
