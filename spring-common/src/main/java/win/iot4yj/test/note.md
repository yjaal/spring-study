# powerMock 学习

## 1、使用普通的Junit测试

```java
package win.iot4yj.test;
import win.iot4yj.entity.User;

/**
 * 抛出异常模拟dao层不可用
 */
public class TestUserDao1 {

    public int getCount() {
        throw new UnsupportedOperationException();
    }

    public void insertUser(User user) {
        throw new UnsupportedOperationException();
    }
}
```



```java
package win.iot4yj.test;

import win.iot4yj.entity.User;

public class TestUserService1 {

    private TestUserDao1 userDao;

    public TestUserService1(TestUserDao1 userDao) {
        this.userDao = userDao;
    }

    public int queryUserCount() {
        return userDao.getCount();
    }

    public void saveUser(User user) {
        userDao.insertUser(user);
    }
}
```



```java
package win.iot4yj.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import win.iot4yj.entity.User;

public class TestUserService1Test {

    private TestUserService1 userService;

    @Before
    public void setup() {
        userService = new TestUserService1(new TestUserDao1());
    }

    @Test
    public void queryUserCount() throws Exception {
        try {
            userService.queryUserCount();
            fail("should not process to here");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

    }

    @Test
    public void saveUser() throws Exception {
        try {
            userService.saveUser(new User());
            fail("should not process to here");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }
}
```

使用`mockito`完成上面的工作

```java
package win.iot4yj.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TestUserService1Test {

    @Mock
    private TestUserDao1 userDao;

    @Test
    public void queryUserCountMock() {
        MockitoAnnotations.initMocks(this);
        TestUserService1 userService = new TestUserService1(userDao);
        //设置方法行为
        Mockito.when(userDao.getCount()).thenReturn(10);
        int count = userService.queryUserCount();
        assertEquals(10, count);
    }
}
```

使用`powermock`完成上面的工作

```java
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
```

这种简单的场景发现三者差别不是很大。





## 局部变量测试

```java
package win.iot4yj.test;

import win.iot4yj.entity.User;

public class TestUserService2 {

    public int queryUserCount() {
        //局部变量
        TestUserDao1 userDao = new TestUserDao1();
        return userDao.getCount();
    }

    public void saveUser(User user) {
        TestUserDao1 userDao = new TestUserDao1();
        userDao.insertUser(user);
    }
}
```



```java
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
```



## 静态方法测试

```java
package win.iot4yj.test;

import win.iot4yj.entity.User;

/**
 * 抛出异常模拟dao层不可用
 */
public class TestUserDao2 {

    public static int getCount() {
        throw new UnsupportedOperationException();
    }

    public static void insertUser(User user) {
        throw new UnsupportedOperationException();
    }
}
```

```java
package win.iot4yj.test;

import win.iot4yj.entity.User;

public class TestUserService3 {

    public int queryUserCount() {
        return TestUserDao2.getCount();
    }

    public void saveUser(User user) {
        TestUserDao2.insertUser(user);
    }
}
```

