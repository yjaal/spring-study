package win.iot4yj.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import win.iot4yj.demo1.PipelineTask;
import win.iot4yj.demo1.PipelineWork;
import win.iot4yj.demo1.TestItem;

public class PipelineTaskTest {

    private static final Logger log = LoggerFactory.getLogger(PipelineTaskTest.class);

    private final Configuration conf = new Configuration();
    private final IOException ioe = new IOException();

    private void runTest(int level, final boolean exp) throws Throwable {
        final List<TestItem> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(new TestItem(i));
        }
        final Object EMPTY = new Object();
        PipelineWork<Object, TestItem> header = new PipelineWork<Object, TestItem>() {
            int idx = 0;

            @Override
            public TestItem doWork(Object obj) throws IOException {
                if (idx < items.size()) {
                    TestItem item = items.get(idx);
                    item.setVal(item.getVal() * 2);
                    if (exp && (idx == items.size() - 1)) {
                        throw ioe;
                    }
                    idx++;
                    log.info(Thread.currentThread().getName() +
                        ":头节点产生(" + level + ") item: " + item.getVal());
                    return item;
                } else {
                    log.info("头节点(" + level + ") 生产完毕");
                    return null;
                }
            }
        };
        PipelineWork<TestItem, TestItem> middle = item -> {
            item.setVal(item.getVal() * 2);
            log.info(Thread.currentThread().getName() + ": 中节点设置(" + level + ")  value: "
                + item.getVal());
            return item;
        };
        PipelineWork<TestItem, Object> trailer = item -> {
            item.setVal(item.getVal() * 2);
            log.info(Thread.currentThread().getName() +
                ": 尾节点设置(" + level + ")  value: " + item.getVal());
            return EMPTY;
        };
        PipelineTask task = new PipelineTask(conf, "pipeline.testcarriage");
        if (level <= 0) {
            return;
        } else if (level == 1) {
            task.appendWork(header, "头节点");
            task.kickOff();
            task.join();
        } else if (level == 2) {
            task.appendWork(header, "头节点").appendWork(trailer, "尾节点");
            task.kickOff();
            task.join();
        } else {
            task.appendWork(header, "头节点");
            for (int i = 0; i < level - 2; i++) {
                task.appendWork(middle, "中节点" + i);
            }
            task.appendWork(trailer, "尾节点");
            task.kickOff();
            task.join();
        }
        if (task.getException() != null) {
            throw task.getException();
        }
        for (int i = 0; i < items.size(); i++) {
            Assert.assertTrue(items.get(i).getVal() == i * Math.pow(2, level));
        }
    }

    @Test
    public void basicTests() throws Throwable {
        for (int i = 1; i < 8; i++) {
            runTest(i, false);
        }
//        runTest(3, false);
    }

    @Test
    public void testException() throws InterruptedException {
        try {
            runTest(3, true);
        } catch (Throwable t) {
            Assert.assertTrue(t == ioe);
            return;
        }
        Assert.assertTrue(false);
    }
}