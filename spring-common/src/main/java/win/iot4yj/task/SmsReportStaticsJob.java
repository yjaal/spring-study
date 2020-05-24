package win.iot4yj.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


/**
 * @author lc
 * @Description: 每分钟/短信报文统计发送状态
 * @date 2018年4月10日 每2分钟统计一次
 */
@Component
public class SmsReportStaticsJob extends QuartzJobBean {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(SmsReportStaticsJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("开始执行-定时短信发送报文统计：{}");
        try {
            SchedulerContext cont = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) cont.get("applicationContextKey");
            // 调用具体sercive业务代码
//            SmsLogStatusReportService service = appCtx.getBean(SmsLogStatusReportService.class);
//            service.smsReportStatis();
        } catch (Exception e) {
            log.error("每分钟/短信报文跑批统计发送状态异常！{}", e);
        }
        log.info("执行结束-定时短信发送报文统计：{}");
    }
}
