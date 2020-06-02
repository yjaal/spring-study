package win.iot4yj.test;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestUserService7.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.crypto.*"})
public class TestUserService7Test {

    @Test
    public void foo() throws Exception {
        TestUserService7 userService = PowerMockito.mock(TestUserService7.class);
        //这里其实不会真实调用的
        userService.foo();
    }

    @Test
    public void foo1() throws Exception {
        TestUserService7 userService = spy(new TestUserService7());
        //这里才会真实调用，含义是如果满足断言那么我就用mock的类或方法，
        //如果不满足则使用真实的方法调用
        userService.foo();
    }

    @Test
    public void foo2() throws Exception {
        TestUserService7 userService = spy(new TestUserService7());
        String arg = "hello";
        doNothing().when(userService).foo(arg);
        //满足断言，则会使用mock
//        userService.foo(arg);

        //不满足断言，走真实方法调用
        userService.foo("aaa");
    }

    @Test
    public void check() throws Exception {
        TestUserService7 userService = spy(new TestUserService7());
        //注意：这里如果写成when...thenReturn 就还是会调用真实对象。同时我们在调用的时候如果参数不一致也会调用真实方法
        doReturn(true).when(userService, "checkExist", Mockito.anyString());
//        when(userService, "checkExist", "aa").thenReturn(true);
        Assert.assertTrue(userService.exist(Mockito.anyString()));
    }

}