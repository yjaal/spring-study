package win.iot4yj.ch5.tpt;

public class DelegatingTerminatableThread extends AbstractTerminableThread {
    private final Runnable task;

    public DelegatingTerminatableThread(Runnable task) {
        this.task = task;
    }

    @Override
    protected void doRun() throws Exception {
        this.task.run();
    }

    public static AbstractTerminableThread of(Runnable task) {
        DelegatingTerminatableThread ret = new DelegatingTerminatableThread(
                task);
        return ret;
    }
}
