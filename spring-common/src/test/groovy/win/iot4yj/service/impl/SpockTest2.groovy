package win.iot4yj.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import win.iot4yj.service.UserService

@SpringBootTest
class SpockTest2 extends Specification {

    @Autowired
    private UserService userService

    def "test_UserService_getUserById"() {
        given:
        def user = userService.getUserById("00424556")
        expect:
        user.name == 'yj'
    }
}