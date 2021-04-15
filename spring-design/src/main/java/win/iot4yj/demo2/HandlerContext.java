package win.iot4yj.demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * todo
 *
 * @author YJ
 * @date 2021/3/23
 **/
public class HandlerContext {

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Handler handler;

    //下一个context的引用
    private HandlerContext next;

    public HandlerContext(Handler handler) {
        this.handler = handler;
    }

    public void setNext(HandlerContext ctx) {
        this.next = ctx;
    }

    /**
     * 执行任务的时候向线程池提交一个runnable的任务，任务中调用handler
     */
    public void doWork(Object msg) {
        if (next != null) {
            executor.submit(() -> {
                //把下一个handler的context穿个handler来实现回调
                handler.channelRead(next, msg);
            });
        }
    }

    /**
     * 这里的write操作是给handler调用的，实际上是一个回调方法，当handler处理完数据之后，调用一下nextcontext
     */
    public void write(Object msg) {
        doWork(msg);
    }
}
