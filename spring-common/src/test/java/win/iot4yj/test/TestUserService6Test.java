package win.iot4yj.test;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestUserService6.class, TestUserDao6.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.crypto.*"})
public class TestUserService6Test {

    @Test
    public void find() throws Exception {
        TestUserDao6 userDao = mock(TestUserDao6.class);
        whenNew(TestUserDao6.class).withAnyArguments().thenReturn(userDao);
        when(userDao.query("aa")).thenReturn("bb");
        TestUserService6 userService = new TestUserService6();
        String result1 = userService.find("aa");
        Assert.assertEquals("bb", result1);

        //这样写太麻烦了
        when(userDao.query("cc")).thenReturn("dd");
        String result2 = userService.find("cc");
        Assert.assertEquals("dd", result2);
    }

    @Test
    public void find1() throws Exception {
        TestUserDao6 userDao = mock(TestUserDao6.class);
        whenNew(TestUserDao6.class).withAnyArguments().thenReturn(userDao);
        when(userDao.query(Matchers.argThat(new MyArgumentMatcher()))).thenReturn("cc");
        TestUserService6 userService = new TestUserService6();

        Assert.assertEquals("cc", userService.find("aa"));
        Assert.assertEquals("cc", userService.find("bb"));
    }

    static class MyArgumentMatcher implements ArgumentMatcher<String> {

        @Override
        public boolean matches(String arg) {
            switch (arg) {
                case "aa":
                case "bb":
                    return true;
                default:
                    return false;
            }
        }
    }

    @Test
    public void find2() throws Exception {
        TestUserDao6 userDao = mock(TestUserDao6.class);
        whenNew(TestUserDao6.class).withAnyArguments().thenReturn(userDao);
        when(userDao.query(Mockito.anyString())).then(invocation -> {
            String arg = (String) invocation.getArguments()[0];
            switch (arg) {
                case "aa":
                    return "bb";
                case "cc":
                    return "dd";
                default:
                    throw new RuntimeException();
            }
        });
        TestUserService6 userService = new TestUserService6();
        Assert.assertEquals("bb", userService.find("aa"));
        Assert.assertEquals("dd", userService.find("cc"));
    }
}