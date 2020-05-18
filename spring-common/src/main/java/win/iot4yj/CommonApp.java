package win.iot4yj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//启动定时任务
@EnableScheduling
public class CommonApp {

    public static void main(String[] args) {
        SpringApplication.run(CommonApp.class);
    }

}
