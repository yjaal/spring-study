package win.iot4yj.test;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestUserService5.class, TestUserDao5.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.crypto.*"})
public class TestUserService5Test {

    @Test
    public void save() throws Exception{
        TestUserDao5 userDao = mock(TestUserDao5.class);
        String username = "aa";
        String passwrod = "bb";
        whenNew(TestUserDao5.class).withArguments(username, passwrod).thenReturn(userDao);
        doNothing().when(userDao).insert();
        TestUserService5 userService = new TestUserService5();
        userService.save(username, passwrod);
        Mockito.verify(userDao).insert();
    }
}