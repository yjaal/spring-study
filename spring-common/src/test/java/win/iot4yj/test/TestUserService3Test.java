package win.iot4yj.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import win.iot4yj.entity.User;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestUserService3.class, TestUserDao2.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.crypto.*"})
public class TestUserService3Test {

    @Test
    public void test2() {
        User user = new User();
        PowerMockito.mockStatic(TestUserDao2.class);
        PowerMockito.verifyStatic(TestUserDao2.class, Mockito.times(0));
        TestUserDao2.insertUser(user);
        TestUserDao2.insertUser(user);
        PowerMockito.verifyStatic(TestUserDao2.class, Mockito.times(1));
        TestUserDao2.insertUser(user);
    }

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
        // 参考：https://github.com/powermock/powermock/pull/837
        // mockStatic does not reset mocking process anymore. As result you may get
        // the UnfinishedVerificationException or UnfinishedStubbingException
        userService.saveUser(user);
        PowerMockito.verifyStatic(TestUserDao2.class, Mockito.times(1));
    }
}