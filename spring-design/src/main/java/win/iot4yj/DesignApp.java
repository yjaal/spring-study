package win.iot4yj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesignApp {

    public static void main(String[] args) {
        SpringApplication.run(DesignApp.class);
    }
}
