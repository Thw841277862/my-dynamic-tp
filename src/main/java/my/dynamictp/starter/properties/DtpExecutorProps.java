package my.dynamictp.starter.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import my.dynamictp.starter.enums.QueueTypeEnum;

/**
 * ThreadPool 的主要属性
 */
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class DtpExecutorProps extends TpExecutorProps {
    /**
     * 线程池的类型 {@link my.dynamictp.starter.enums.ExecutorTypeEnum}
     */
    private String executorType;
    /**
     * 线程池队列类型 {@link QueueTypeEnum}
     */
    private String queueType = QueueTypeEnum.ARRAY_BLOCKING_QUEUE.getName();
    /**
     * 阻塞队列容量
     */
    private int queueCapacity = 1024;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix = "my-dtp";

    /**
     * 检查核心参数
     */
    public boolean coreParamIsInValid() {
        return this.getCorePoolSize() < 0
                || this.getMaximumPoolSize() <= 0
                || this.getMaximumPoolSize() < this.getCorePoolSize()
                || this.getKeepAliveTime() < 0;
    }
}
