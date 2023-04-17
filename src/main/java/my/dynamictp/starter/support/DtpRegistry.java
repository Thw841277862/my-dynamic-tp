package my.dynamictp.starter.support;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import my.dynamictp.starter.constant.ExecutorConverter;
import my.dynamictp.starter.enums.RejectedTypeEnum;
import my.dynamictp.starter.properties.DtpExecutorProps;
import my.dynamictp.starter.properties.DtpProperties;
import my.dynamictp.starter.thread.DtpExecutor;
import my.dynamictp.starter.util.TpMainFields;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DtpRegistry {
    private static DtpProperties dtpProperties;

    private static final Map<String, ExecutorWrapper> EXECUTOR_REGISTRY = new ConcurrentHashMap<>();

    public DtpRegistry(DtpProperties dtpProperties) {
        DtpRegistry.dtpProperties = dtpProperties;
    }

    /**
     * 注册线程池到本地容器
     *
     * @param wrapper–新创建的ThreadPoolExecutor包装器实例
     * @param source–调用register方法的源
     */
    public static void registerExecutor(ExecutorWrapper wrapper, String source) {
        log.info("DynamicTp register dtpExecutor, source: {}, executor: {}",
                source, ExecutorConverter.convert(wrapper));
        EXECUTOR_REGISTRY.putIfAbsent(wrapper.getThreadPoolName(), wrapper);
    }

    public static void refresh(DtpProperties dtpProperties) {
        if (Objects.isNull(dtpProperties) || CollectionUtils.isEmpty(dtpProperties.getExecutors())) {
            log.warn("DynamicTp refresh, empty threadPool properties.");
            return;
        }
        dtpProperties.getExecutors().forEach(x -> {
            if (StringUtils.isBlank(x.getThreadPoolName())) {
                log.warn("DynamicTp refresh, threadPoolName must not be empty.");
                return;
            }
            ExecutorWrapper executorWrapper = EXECUTOR_REGISTRY.get(x.getThreadPoolName());
            if (Objects.nonNull(executorWrapper)) {
                refresh(executorWrapper, x);
                return;
            }
            log.warn("DynamicTp refresh, cannot find specified executor, name: {}.", x.getThreadPoolName());
        });
    }

    private static void refresh(ExecutorWrapper executorWrapper, DtpExecutorProps props) {
        if (props.coreParamIsInValid()) {
            log.error("DynamicTp refresh, invalid parameters exist, properties: {}", props);
            return;
        }
        TpMainFields oldFields = ExecutorConverter.convert(executorWrapper);
        doRefresh(executorWrapper, props);
        TpMainFields newFields = ExecutorConverter.convert(executorWrapper);
        if (oldFields.equals(newFields)) {
            log.debug("DynamicTp refresh, main properties of [{}] have not changed.",
                    executorWrapper.getThreadPoolName());
            return;
        }

    }

    private static void doRefresh(ExecutorWrapper executorWrapper, DtpExecutorProps props) {
        ExecutorAdapter<?> executor = executorWrapper.getExecutor();
        doRefreshPoolSize(executor, props);
        if (!Objects.equals(executor.getKeepAliveTime(props.getUnit()), props.getKeepAliveTime())) {
            executor.setKeepAliveTime(props.getKeepAliveTime(), props.getUnit());
        }
        // update queue
        updateQueueProps(executor, props);

        if (executor instanceof DtpExecutor) {
            doRefreshDtp(executorWrapper, props);
            return;
        }
    }
    private static void doRefreshPoolSize(ExecutorAdapter<?> executor, DtpExecutorProps props) {
        if (props.getMaximumPoolSize() < executor.getMaximumPoolSize()) {
            if (!Objects.equals(executor.getCorePoolSize(), props.getCorePoolSize())) {
                executor.setCorePoolSize(props.getCorePoolSize());
            }
            if (!Objects.equals(executor.getMaximumPoolSize(), props.getMaximumPoolSize())) {
                executor.setMaximumPoolSize(props.getMaximumPoolSize());
            }
            return;
        }
        if (!Objects.equals(executor.getMaximumPoolSize(), props.getMaximumPoolSize())) {
            executor.setMaximumPoolSize(props.getMaximumPoolSize());
        }
        if (!Objects.equals(executor.getCorePoolSize(), props.getCorePoolSize())) {
            executor.setCorePoolSize(props.getCorePoolSize());
        }
    }

    private static void updateQueueProps(ExecutorAdapter<?> executor, DtpExecutorProps props) {

        BlockingQueue<Runnable> queue = executor.getQueue();
        log.warn("DynamicTp refresh, the blockingqueue capacity cannot be reset, poolName: {}, queueType {}",
                props.getThreadPoolName(), queue.getClass().getSimpleName());
        return;
    }


    private static void doRefreshDtp(ExecutorWrapper executorWrapper, DtpExecutorProps props) {
        DtpExecutor executor = (DtpExecutor) executorWrapper.getExecutor();
        // update reject handler
        if (!Objects.equals(executor.getRejectHandlerName(), props.getRejectedHandlerType())) {
            executor.setRejectedExecutionHandler(RejectedTypeEnum.buildRejectedHandler(props.getRejectedHandlerType()));
            executor.setRejectHandlerName(props.getRejectedHandlerType());
        }

    }
}
