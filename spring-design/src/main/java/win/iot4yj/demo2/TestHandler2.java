package win.iot4yj.demo2;

/**
 * todo
 *
 * @author YJ
 * @date 2021/3/23
 **/
public class TestHandler2 implements Handler {

    @Override
    public void channelRead(HandlerContext ctx, Object msg) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = (String) msg + "-handler2";
        System.out.println(result);
        ctx.write(result);
    }
}
