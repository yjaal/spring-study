package win.iot4yj.task;

import java.util.Objects;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 这就是定时任务持久化，也就是有状态定时任务，可以将之前运行的参数记录下来，通过count值可以发现是累加的
 */
@PersistJobDataAfterExecution
public class HiJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(HiJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Object value = jobDataMap.get("jobParam");
        log.error("Hi quartz job，jobParam:{}", value);
        Integer count = (Integer) jobDataMap.get("count");
        jobDataMap.put("count", Objects.equals(null, count) ? 0 : count++);
        log.error("count:{}", count);
    }
}
