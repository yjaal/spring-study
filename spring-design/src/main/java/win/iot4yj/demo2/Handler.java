package win.iot4yj.demo2;

/**
 * todo
 *
 * @author YJ
 * @date 2021/3/23
 **/
public interface Handler {

    void channelRead(HandlerContext ctx, Object msg);
}
