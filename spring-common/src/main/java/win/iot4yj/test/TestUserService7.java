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
