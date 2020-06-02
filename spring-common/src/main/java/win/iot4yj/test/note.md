# powerMock 学习

## 使用普通的 Junit 测试

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

    @Test(expected = UnsupportedOperationException.class)
    public void saveUser() throws Exception {
        userService.saveUser(new User());
        fail("should not process to here");
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

```java
package win.iot4yj.test;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
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
        // 参考：https://github.com/powermock/powermock/pull/837
        // mockStatic does not reset mocking process anymore. As result you may get
        // the UnfinishedVerificationException or UnfinishedStubbingException
        PowerMockito.verifyStatic(TestUserDao2.class, Mockito.times(1));
    }
}
```

这里在使用`verifyStatic`方法的时候要注意，在`powermock1.x`版本的时候可以直接使用`verifyStatic()`即可，但是在`2.x`版本则需要传入`mockClass`，但是上面的例子依然报错，具体原因不详，后面再研究。如果要成功则需要这样：

```java
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
```

这里紧跟`verifyStatic`调用的`TestUserDao2.insertUser(user);`其实是没有执行的，不推荐这种用法，后面可以使用`answer`。



## verify 使用

```java
package win.iot4yj.test;

import win.iot4yj.entity.User;

/**
 * 抛出异常模拟dao层不可用
 */
public class TestUserDao4 {

    public int getCount(User user) {
        throw new UnsupportedOperationException();
    }

    public void update(User user) {
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

public class TestUserService4 {

    public void saveOrUpdate(User user) {
        TestUserDao4 userDao = new TestUserDao4();
        if (userDao.getCount(user) > 0) {
            userDao.update(user);
        } else {
            userDao.insertUser(user);
        }
    }
}
```

```java
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
```



## mock 不同的构造函数

```java
package win.iot4yj.test;

/**
 * 抛出异常模拟dao层不可用
 */
public class TestUserDao5 {

    private String username;

    private String password;

    public void insert() {
        throw new UnsupportedOperationException();
    }


    public TestUserDao5(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

```java
package win.iot4yj.test;

public class TestUserService5 {

    public void save(String username, String password) {
        TestUserDao5 userDao = new TestUserDao5(username, password);
        userDao.insert();
    }
}
```

```java
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
```

这里注意：如果后面调用`userService.save`方法的时候将参数改变了，那么就会失败。



## ArgumentMatcher 使用

```java
package win.iot4yj.test;

/**
 * 抛出异常模拟dao层不可用
 */
public class TestUserDao6 {

    public String query(String username) {
        throw new UnsupportedOperationException();
    }
}
```

```java
package win.iot4yj.test;

public class TestUserService6 {

    public String find(String username) {
        TestUserDao6 userDao = new TestUserDao6();
        return userDao.query(username);
    }
}
```

```java
package win.iot4yj.test;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
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

}
```



## Answer 使用

上面我们可以看到，所有情况下的返回值都是同样的，如果我想对应不同的返回值怎么做呢？

```java
// TestUserService6Test
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
```



## Spy 的使用

```java
package win.iot4yj.test;

public class TestUserService7 {

    private void log() {
        System.out.println("this is log");
    }

    public void foo() {
        log();
    }

    public void foo(String arg) {
        log();
    }

    public boolean exist(String username) {
        return checkExist(username);
    }

    private boolean checkExist(String username) {
        throw new UnsupportedOperationException();
    }
}
```

```java
package win.iot4yj.test;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        doReturn(true).when(userService, "checkExist", "aa");
        Assert.assertTrue(userService.exist("aa"));
    }

}
```

通过上面的例子我们不仅仅知道了`Spy`的用法，同时对于私有方法的`mock`也包含在内了。





