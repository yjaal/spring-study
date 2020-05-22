package win.iot4yj.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class HiJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(HiJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.error("Hi quartz job");
    }
}
