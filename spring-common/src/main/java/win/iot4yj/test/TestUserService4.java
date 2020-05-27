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
