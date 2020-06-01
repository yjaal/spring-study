package win.iot4yj.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "UserController")
public class UserController {

    @GetMapping("hello")
    public String hello(String msg) {
        return "hello " + msg;
    }
}

