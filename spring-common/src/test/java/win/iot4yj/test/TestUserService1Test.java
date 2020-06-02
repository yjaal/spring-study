package win.iot4yj.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import win.iot4yj.entity.User;

public class TestUserService1Test {

    @Test
    public void queryUserCountPowerMock() {
        TestUserDao1 userDao = PowerMockito.mock(TestUserDao1.class);
//        PowerMockito.doReturn(10).when(userDao).getCount();
        PowerMockito.when(userDao.getCount()).thenReturn(10);
        TestUserService1 userService = new TestUserService1(userDao);
        int count = userService.queryUserCount();
        assertEquals(10, count);
    }

    @Test
    public void savePowerMock() {
        TestUserDao1 userDao = PowerMockito.mock(TestUserDao1.class);
        User user = new User();
        //设置行为，调用的时候什么都不做
        PowerMockito.doNothing().when(userDao).insertUser(user);
        TestUserService1 userService = new TestUserService1(userDao);
        userService.saveUser(user);

        //那如何知道确实有调用insertUser方法呢？
        Mockito.verify(userDao).insertUser(user);
    }
}