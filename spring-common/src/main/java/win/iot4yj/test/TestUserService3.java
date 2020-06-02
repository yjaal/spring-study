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
