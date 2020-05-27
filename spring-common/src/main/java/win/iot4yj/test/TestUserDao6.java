package win.iot4yj.test;

/**
 * 抛出异常模拟dao层不可用
 */
public class TestUserDao6 {

    public String query(String username) {
        throw new UnsupportedOperationException();
    }
}
