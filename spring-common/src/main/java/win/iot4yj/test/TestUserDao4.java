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
