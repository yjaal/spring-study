package win.iot4yj.test;

public class TestUserService6 {

    public String find(String username) {
        TestUserDao6 userDao = new TestUserDao6();
        return userDao.query(username);
    }
}
