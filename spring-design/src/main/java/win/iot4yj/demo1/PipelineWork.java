package win.iot4yj.demo1;

public interface PipelineWork<K, T> {

    T doWork(K k) throws Exception;
}
