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
