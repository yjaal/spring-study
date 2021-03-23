package win.iot4yj.demo1;

/**
 * todo
 *
 * @author YJ
 * @date 2021/3/22
 **/
public class TestItem {

    private volatile int val;

    public TestItem(int i) {
        val = i;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int inVal) {
        val = inVal;
    }
}
