package my.dynamictp.starter.support;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

public interface ExecutorAdapter<E extends Executor> extends Executor {
    /**
     * 获取源线程池执行器
     *
     * @return the original executor
     */
    E getOriginal();

    /**
     * 执行方法
     */
    @Override
    default void execute(Runnable command) {
        getOriginal().execute(command);
    }

    /**
     * 获取核心线程数
     */
    int getCorePoolSize();

    /**
     * 设置核心线程数
     */
    void setCorePoolSize(int corePoolSize);

    /**
     * 获取最大核心线程数
     */
    int getMaximumPoolSize();

    /**
     * 设置最大核心线程数
     */
    void setMaximumPoolSize(int maximumPoolSize);

    /**
     * 获取线程数
     *
     * @return the pool size
     */
    int getPoolSize();

    /**
     * 获取存活的线程数
     */
    int getActiveCount();

    long getKeepAliveTime(TimeUnit unit);

    BlockingQueue<Runnable> getQueue();

    default boolean allowsCoreThreadTimeOut() {
        //default unsupported
        return false;
    }

    default String getRejectHandlerName() {
        return Optional.ofNullable(getRejectedExecutionHandler())
                .map(h -> h.getClass().getSimpleName())
                .orElse("unsupported");
    }

    default void setKeepAliveTime(long time, TimeUnit unit) {
        //default unsupported
    }

    RejectedExecutionHandler getRejectedExecutionHandler();
}
