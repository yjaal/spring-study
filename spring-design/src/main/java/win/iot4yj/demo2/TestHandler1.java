package win.iot4yj.demo2;

import java.util.Random;

/**
 * todo
 *
 * @author YJ
 * @date 2021/3/23
 **/
public class TestHandler1 implements Handler {

    @Override
    public void channelRead(HandlerContext ctx, Object msg) {
        try {
            // 这里阻塞时间一定要弄成随机的，这样才能看出效果
            Thread.sleep(new Random().nextInt(10) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //在字符串后面加特定字符串
        String result = (String) msg + "-handler1";
        System.out.println(result);
        //写入操作，这个操作是必须的，相当于将结果传递给下一个handler
        ctx.write(result);
    }
}
