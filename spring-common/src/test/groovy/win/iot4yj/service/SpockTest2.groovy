package win.iot4yj.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(locations = ["classpath*:application.yml"])
class SpockTest2 extends Specification {

    @Autowired
//    @Subject
    private UserService userService

    def "test_UserService_getUserById"() {
        expect:
        user.name == 'yj'
        user.sex == '1'
        where:
        user = userService.getUserById("001")
    }

}