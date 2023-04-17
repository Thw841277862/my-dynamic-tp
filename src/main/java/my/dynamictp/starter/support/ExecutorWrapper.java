package my.dynamictp.starter.support;

import lombok.Data;
import my.dynamictp.starter.thread.DtpExecutor;


@Data
public class ExecutorWrapper {
    /**
     * 线程池名称
     */
    private String threadPoolName;


    /**
     * Executor.
     */
    private ExecutorAdapter<?> executor;

    public ExecutorWrapper() {
    }

    public ExecutorWrapper(DtpExecutor executor) {
        this.threadPoolName = executor.getThreadPoolName();
        this.executor = executor;
    }

    public static ExecutorWrapper of(DtpExecutor executor) {
        return new ExecutorWrapper(executor);
    }
}
