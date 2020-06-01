package win.iot4yj.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest
class UserControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def "test controller"() {
        expect: "Status is 200 and the response is 'Hello world!'"
        mockMvc.perform(MockMvcRequestBuilders.get("/user/hello?msg=world"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .response
                .contentAsString == "hello world"
    }
}