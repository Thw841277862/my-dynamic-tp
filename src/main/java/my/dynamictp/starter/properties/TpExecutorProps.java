package my.dynamictp.starter.properties;

import lombok.Data;
import my.dynamictp.starter.enums.RejectedTypeEnum;

import java.util.concurrent.TimeUnit;

/**
 * 线程池基础属性
 */
@Data
public class TpExecutorProps {
    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 线程池别名
     */
    private String threadPoolAliasName;
    /**
     * 核心线程数
     */
    private int corePoolSize = 1;
    /**
     * 最大线程数
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 当线程数大于核心数时，这是多余的空闲线程在终止前等待新任务的最长时间。
     */
    private long keepAliveTime = 60;
    /**
     * 超时时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;


    /**
     * 拒绝策略模式 type, see {@link RejectedTypeEnum}
     */
    private String rejectedHandlerType = RejectedTypeEnum.ABORT_POLICY.getName();
}
