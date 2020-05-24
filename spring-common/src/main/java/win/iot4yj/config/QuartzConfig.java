package win.iot4yj.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import win.iot4yj.task.HiJob;

/**
 * quartz定时任务配置类。quartz能做的事很多，如发邮件、短信等。还可以配合spring batch对数据定时批量处理。
 * 深入内容可以参考：龙果学院。
 * 一个Job可以绑定到多个Trigger上面，但是一个Trigger不能触发多个Job
 * @author yj
 */
@Configuration
public class QuartzConfig {

    /**
     * 用于定义任务的一些属性信息
     */
    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(HiJob.class)
            //设置定时任务名字和分组，也可以不设置，这样则随机生成一个定时任务名字
            .withIdentity("myJob1", "myJobGroup1")
            //JobDataMap可以给任务execute传递参数，就是为Job设置一些属性
            .usingJobData("jobParam", "jobParam1")
            .storeDurably()
            .build();
    }

    /**
     * 触发器：设置出发频率等。一般有两种触发器，一种是下面这种Simple Trigger，按固定频率触发定时任务。
     * 另一种是CronTrigger，这种触发器可以设置日历时间，在什么时间触发，什么时间截止
     */
    @Bean
    public Trigger myTrigger() {
        return TriggerBuilder.newTrigger()
            //一个触发器可以触发多个定时任务
            .forJob(myJobDetail())
            .withIdentity("myTrigger1", "myTriggerGroup1")
            .usingJobData("jobTriggerParam", "jobTriggerParam1")
            .startNow()
            //下面是两种配置定时任务执行频率的方式
            //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
            .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
            .build();
    }

}
