package win.iot4yj.demo1;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管道模式任务, https://blog.csdn.net/Androidlushangderen/article/details/100427169
 *
 * @author YJ
 * @date 2021/3/22
 **/
public class PipelineTask {

    private static final Logger log = LoggerFactory.getLogger(PipelineTask.class);

    private LinkedList<Carriage> pipeLine;
    private Configuration conf;
    private String confPrefix;


    private class Carriage<K, T> {

        private PipelineWork<K, T> work;
        private ArrayList<T>[] transferList;
        private ReentrantLock[] lock;
        private Throwable[] exception;
        // 每个线程是否执行完的标志
        private boolean[] done;
        // 执行线程数
        private int numThreads;
        private int blockingThreshold, transferThreshold, logThreshold;
        // 此节点依赖的前节点
        private Carriage prev;
        private volatile boolean shouldStop = false;
        final private List<T> EMPTY_LIST = new ArrayList<>(0);
        private boolean header = false;
        private boolean trailer = false;
        // 执行当前节点的线程组
        private Thread[] threads = null;
        private String name = null;


        private class CarriageThread extends Thread {

            // 线程标志
            private int index;

            public CarriageThread(int i) {
                index = i;
                this.setName("PipelineTask-" + name + "-" + index);
            }

            @Override
            public void run() {
                try {
                    // 如果当前节点是头节点，则header=true，prev=null，检查结果为true，继续执行
                    // 如果当前节点不是头节点，则header=false，prev！=null，检查结果为true，继续执行
                    Preconditions.checkState((prev != null) ^ header);
                    ArrayList<T> localList = null;
                    if (!trailer) {
                        localList = new ArrayList<>(Math.max(blockingThreshold, 0));
                    }
                    log.info("Carriage " + name + " thread " + index + " is scheduled");
                    int itemHandled = 0;
                    // 是否应该停止
                    while (!shouldStop) {
                        if (header) {
                            // 头节点无前置节点
                            T t = work.doWork(null);
                            itemHandled++;
                            if (itemHandled == logThreshold) {
                                log.info("Handled " + itemHandled + " in " + name + " thread "
                                    + index);
                                itemHandled = 0;
                            }
                            if (!trailer && t != null) {
                                localList.add(t);
                            } else if (t == null) {
                                break;
                            }
                        } else {
                            List<K> inputList = prev.getList();
                            if (inputList == null) {
                                break;
                            } else if (inputList.isEmpty()) {
                                Thread.yield();
                                continue;
                            }
                            for (K k : inputList) {
                                T t = work.doWork(k);
                                itemHandled++;
                                if (itemHandled == logThreshold) {
                                    log.info("Handled " + itemHandled + " in " + name +
                                        " thread " + index);
                                    itemHandled = 0;
                                }
                                if (!trailer && t != null) {
                                    localList.add(t);
                                }
                            }
                        }
                        if (trailer) {
                            continue;
                        }
                        if (localList.size() >= transferThreshold) {
                            if (lock[index].tryLock()) {
                                if (transferList[index] == null) {
                                    transferList[index] = localList;
                                    lock[index].unlock();
                                    localList =
                                        new ArrayList<T>(
                                            Math.max(blockingThreshold, 0));
                                } else {
                                    lock[index].unlock();
                                }
                            }
                        }
                        if (blockingThreshold > 0 && localList.size() >= blockingThreshold) {
                            lock[index].lock();
                            while (transferList[index] != null) {
                                lock[index].unlock();
                                Thread.yield();
                                lock[index].lock();
                            }
                            transferList[index] = localList;
                            lock[index].unlock();
                            localList = new ArrayList<T>(blockingThreshold);
                        }
                    }
                    lock[index].lock();
                    while (!trailer) {
                        if (localList.size() > 0) {
                            if (transferList[index] == null) {
                                transferList[index] = localList;
                                break;
                            } else {
                                lock[index].unlock();
                                Thread.yield();
                                lock[index].lock();
                                continue;
                            }
                        } else {
                            break;
                        }
                    }
                    done[index] = true;
                    if (prev != null) {
                        exception[index] = prev.getException();
                    }
                    lock[index].unlock();
                } catch (Throwable t) {
                    log.warn("Exception in pipeline task ", t);
                    lock[index].lock();
                    exception[index] = t;
                    lock[index].unlock();
                    shouldStop = true;
                }
                log.info("Carriage " + name + " thread " + index + " is done");
            }

        }

        public Carriage(PipelineWork<K, T> inWork, String caName) {
            name = caName;
            blockingThreshold = conf.getInt(getBlockingThresholdKey(name), 10000);
            transferThreshold = conf.getInt(getTransferThresholdKey(name), 100);
            logThreshold = conf.getInt(getLoggingThresholdKey(name), 10000000);
            numThreads = conf.getInt(getNumThreadsKey(name), 1);

            if (blockingThreshold < 0 || transferThreshold < 0
                || blockingThreshold < transferThreshold || logThreshold < 0
                || numThreads < 0) {
                throw new IllegalArgumentException("Illegal argument for PipelineTask " + name);
            }
            work = inWork;
            transferList = new ArrayList[numThreads];
            for (int i = 0; i < numThreads; i++) {
                transferList[i] = null;
            }
            lock = new ReentrantLock[numThreads];
            for (int i = 0; i < numThreads; i++) {
                lock[i] = new ReentrantLock();
            }
            exception = new Throwable[numThreads];
            for (int i = 0; i < numThreads; i++) {
                exception[i] = null;
            }
            done = new boolean[numThreads];
            for (int i = 0; i < numThreads; i++) {
                done[i] = false;
            }
        }

        public void setPrev(Carriage ca) {
            prev = ca;
            Preconditions.checkState(header == false);
        }

        public void stopCarriager() {
            shouldStop = true;
        }

        public void setHeader() {
            header = true;
            Preconditions.checkState(prev == null);
        }

        public void setTrailer() {
            trailer = true;
        }

        /**
         * 获取本地处理结果
         */
        public List<T> getList() {
            int doneThread = 0;
            for (int i = 0; i < transferList.length; i++) {
                // 上锁
                lock[i].lock();
                try {
                    if (exception[i] != null) {
                        return null;
                    }
                    if (transferList[i] != null) {
                        ArrayList<T> res = transferList[i];
                        transferList[i] = null;
                        return res;
                    } else {
                        if (done[i]) {
                            doneThread++;
                        }
                    }
                } finally {
                    lock[i].unlock();
                }
            }
            if (doneThread == transferList.length) {
                return null;
            } else {
                return EMPTY_LIST;
            }
        }

        public Throwable getException() {
            for (int i = 0; i < transferList.length; i++) {
                lock[i].lock();
                try {
                    if (exception[i] != null) {
                        return exception[i];
                    }
                } finally {
                    lock[i].unlock();
                }
            }
            return null;
        }

        public void kickOff() {
            threads = new Thread[numThreads];
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new CarriageThread(i);
                threads[i].start();
            }
        }

        public void join() throws InterruptedException {
            for (int i = 0; i < numThreads; i++) {
                threads[i].join();
            }
        }
    }

    public PipelineTask(Configuration inConf, String prefix) {
        pipeLine = new LinkedList<>();
        confPrefix = prefix;
        conf = inConf;
    }

    public PipelineTask appendWork(PipelineWork work, String name) {
        Carriage ca = new Carriage(work, name);
        pipeLine.add(ca);
        return this;
    }

    public void kickOff() {
        for (int i = 0; i < pipeLine.size(); i++) {
            Carriage ca = pipeLine.get(i);
            if (i == 0) {
                // 将自己设置为头节点
                ca.setPrev(null);
                ca.setHeader();
            }
            if (i == pipeLine.size() - 1) {
                // 将自己设置为尾节点
                ca.setTrailer();
            }
            if (i > 0) {
                ca.setPrev(pipeLine.get(i - 1));
            }
            ca.kickOff();
        }
    }

    public void join() throws InterruptedException {
        Carriage ca = pipeLine.getLast();
        ca.join();
    }

    public Throwable getException() {
        Carriage ca = pipeLine.getLast();
        return ca.getException();
    }

    public String getBlockingThresholdKey(String pipeName) {
        return confPrefix + "." + pipeName + "." + "blocking";
    }

    public String getTransferThresholdKey(String pipeName) {
        return confPrefix + "." + pipeName + "." + "transfer";
    }

    public String getLoggingThresholdKey(String pipeName) {
        return confPrefix + "." + pipeName + "." + "transfer";
    }

    public String getNumThreadsKey(String pipeName) {
        return confPrefix + "." + pipeName + "." + "threads";
    }
}
