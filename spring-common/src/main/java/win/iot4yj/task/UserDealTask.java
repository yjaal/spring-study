package win.iot4yj.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDealTask {

    /**
     * 每6s执行一次
     */
    @Scheduled(cron = "*/6 * * * * ?")
    private void first() {
        log.error("hello spring task");
    }
}
