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
