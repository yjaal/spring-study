package win.iot4yj.service


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import spock.lang.Shared
import spock.lang.Specification

@Configuration("application.yml")
@SpringBootTest
class UserServiceTest extends Specification {

    @Shared
    private UserService userService;


    def "测试根据id查询用户方法"() {

    }

}