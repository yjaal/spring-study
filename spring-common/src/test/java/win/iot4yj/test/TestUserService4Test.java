package win.iot4yj.test;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import win.iot4yj.entity.User;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TestUserService4.class, TestUserDao4.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*", "javax.crypto.*"})
public class TestUserService4Test {

    @Test
    public void saveOrUpdate() throws Exception{
        User user = mock(User.class);
        TestUserDao4 userDao = mock(TestUserDao4.class);
        whenNew(TestUserDao4.class).withAnyArguments().thenReturn(userDao);
        //设置调用的时候返回值为0，此时会调用insertUser而不会调用update
        when(userDao.getCount(user)).thenReturn(0);
        TestUserService4 userService = new TestUserService4();
        userService.saveOrUpdate(user);
        Mockito.verify(userDao).insertUser(user);
        Mockito.verify(userDao, Mockito.never()).update(user);
    }
}