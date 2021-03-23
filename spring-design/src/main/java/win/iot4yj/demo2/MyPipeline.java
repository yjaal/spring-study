package win.iot4yj.demo2;

/**
 * https://blog.csdn.net/tjy451592402/article/details/79459013
 *
 * 这个demo不是很完善，比如没有关闭线程池等等， 这个其实可以在尾节点处理完后判断关闭
 *
 * @author YJ
 * @date 2021/3/23
 **/
public class MyPipeline {

    public static void main(String[] args) {
        MyPipeline pipeline = new MyPipeline();
        pipeline.addFirst(new TestHandler2());
        pipeline.addFirst(new TestHandler1());
        for (int i = 0; i < 10; i++) {
            pipeline.Request("hello" + i);
        }
    }

    //链表头
    private HandlerContext head;
    //链表尾，如果是一个双向链表，这个成员将会被用到，netty就使用的双向链表，因为是全双工的。
    private HandlerContext tail;

    //这里仅仅实现了一个简单的插入操作，即在链表的头部出入一个handler。
    public void addFirst(Handler handler) {
        HandlerContext ctx = new HandlerContext(handler);
        HandlerContext tmp = head;
        head = ctx;
        head.setNext(tmp);
    }

    public MyPipeline() {
        head = tail = new HeadContext(new HeadHandler());
    }

    //封装了外部调用接口
    public void Request(Object msg) {
        head.doWork(msg);
    }

    //这是一个内部类，为默认handler的context
    final class HeadContext extends HandlerContext {

        public HeadContext(Handler handler) {
            super(handler);
        }
    }

    //这是一个内部类，是pipeline的默认处理handler。
    final class HeadHandler implements Handler {

        @Override
        public void channelRead(HandlerContext ctx, Object msg) {
            String result = (String) msg + "end";
            System.out.println(result);
        }
    }
}
