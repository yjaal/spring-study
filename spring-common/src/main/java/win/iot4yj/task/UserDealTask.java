package win.iot4yj.task;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * spring基本定时任务：
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#spring-integration
 */
@Component
public class UserDealTask {

    private static final Logger log = LoggerFactory.getLogger(UserDealTask.class);

    /**
     * 每6s执行一次
     */
    @Scheduled(cron = "*/6 * * * * ?")
    private void first() {
        log.error("hello spring task");
    }
}
