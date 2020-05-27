package win.iot4yj.test;

public class TestUserService5 {

    public void save(String username, String password) {
        TestUserDao5 userDao = new TestUserDao5(username, password);
        userDao.insert();
    }
}
