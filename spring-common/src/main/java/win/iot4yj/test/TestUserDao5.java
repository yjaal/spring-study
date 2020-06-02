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
