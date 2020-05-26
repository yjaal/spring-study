package win.iot4yj.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import win.iot4yj.entity.User;

@RunWith(PowerMockRunner.class)
//将某个class的字节码注入进来
@PrepareForTest(TestUserService2.class)
public class TestUserService2Test {

    @Test
    public void queryUserCount() throws Exception{
        try {
            TestUserService2 userService = new TestUserService2();
            TestUserDao1 userDao = PowerMockito.mock(TestUserDao1.class);
            //想要将mock出来的userDao注入到方法体中，必须依赖@RunWith(PowerMockRunner.class)
            //这是通过改变字节码的方式实现的，那需要依赖@PrepareForTest将TestUserService2
            //的字节码注入进来
            PowerMockito.whenNew(TestUserDao1.class).withNoArguments().thenReturn(userDao);
            PowerMockito.when(userDao.getCount()).thenReturn(10);
            int count = userService.queryUserCount();
            assertEquals(10, count);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void saveUser() throws Exception{
        try {
            User user = new User();
            TestUserService2 userService = new TestUserService2();
            TestUserDao1 userDao = PowerMockito.mock(TestUserDao1.class);
            PowerMockito.whenNew(TestUserDao1.class).withAnyArguments().thenReturn(userDao);
            PowerMockito.doNothing().when(userDao).insertUser(user);
            userService.saveUser(user);
            //断言只调用了一次
            Mockito.verify(userDao, Mockito.times(1)).insertUser(user);
        } catch (Exception e) {
            fail();
        }
    }
}