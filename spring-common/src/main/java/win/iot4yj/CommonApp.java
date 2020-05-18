package win.iot4yj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 参考：https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-starter
 */
@SpringBootApplication
//启动定时任务
@EnableScheduling
public class CommonApp {

    public static void main(String[] args) {
        SpringApplication.run(CommonApp.class);
    }

}
