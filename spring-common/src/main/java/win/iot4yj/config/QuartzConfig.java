package win.iot4yj.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import win.iot4yj.task.HiJob;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(HiJob.class)
            .withIdentity("myJob1", "myJobGroup1")
            //JobDataMap可以给任务execute传递参数
            .usingJobData("job_param", "job_param1")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger myTrigger() {
        return TriggerBuilder.newTrigger()
            .forJob(myJobDetail())
            .withIdentity("myTrigger1", "myTriggerGroup1")
            .usingJobData("job_trigger_param", "job_trigger_param1")
            .startNow()
            //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
            .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
            .build();
    }

}
