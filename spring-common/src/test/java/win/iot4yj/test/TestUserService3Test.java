package win.iot4yj.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import win.iot4yj.entity.User;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestUserService3.class, TestUserDao2.class})
public class TestUserService3Test {

    @Test
    public void queryUserCount() {
        PowerMockito.mockStatic(TestUserDao2.class);
        PowerMockito.when(TestUserDao2.getCount()).thenReturn(10);
        TestUserService3 userService = new TestUserService3();
        int count = userService.queryUserCount();
        assertEquals(10, count);
    }

    @Test
    public void saveUser() {
        User user = new User();
        PowerMockito.mockStatic(TestUserDao2.class);
        PowerMockito.doNothing().when(TestUserDao2.class);
        TestUserService3 userService = new TestUserService3();
        userService.saveUser(user);
        PowerMockito.verifyStatic(TestUserDao2.class);
    }
}