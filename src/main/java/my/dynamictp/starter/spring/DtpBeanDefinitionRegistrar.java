package my.dynamictp.starter.spring;

import lombok.extern.slf4j.Slf4j;
import my.dynamictp.starter.enums.ExecutorTypeEnum;
import my.dynamictp.starter.enums.QueueTypeEnum;
import my.dynamictp.starter.enums.RejectedTypeEnum;
import my.dynamictp.starter.properties.DtpExecutorProps;
import my.dynamictp.starter.properties.DtpProperties;
import my.dynamictp.starter.thread.NamedThreadFactory;
import my.dynamictp.starter.util.BeanUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


import static my.dynamictp.starter.constant.DynamicTpConst.THREAD_POOL_NAME;

@Slf4j
public class DtpBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment environment;

    /**
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        DtpProperties dtpProperties = new DtpProperties();
        PropertiesBinder.bindDtpProperties(environment, dtpProperties);
        List<DtpExecutorProps> executors = dtpProperties.getExecutors();
        if (CollectionUtils.isEmpty(executors)) {
            log.warn("DynamicTp registrar, no executors are configured.");
            return;
        }
        executors.forEach(e -> {
            //获取相关自定义线程执行器
            Class<?> executorTypeClass = ExecutorTypeEnum.getClass(e.getExecutorType());
            //给定相关线程产生
            Map<String, Object> propertyValues = buildPropertyValues(e);
            //给DtpExecutor构造参数
            Object[] objects = buildConstructorArgs(executorTypeClass, e);
            //以线程名称注册相关线程池到spring容器中
            BeanUtil.registerIfAbsent(registry, e.getThreadPoolName(), executorTypeClass, propertyValues, objects);
        });
    }

    private Map<String, Object> buildPropertyValues(DtpExecutorProps executorProps) {
        Map<String, Object> propertyValues = new HashMap<>();
        propertyValues.put(THREAD_POOL_NAME, executorProps.getThreadPoolName());
        return propertyValues;
    }

    private Object[] buildConstructorArgs(Class<?> clazz, DtpExecutorProps executorProps) {

        BlockingQueue<Runnable> blockingQueue = QueueTypeEnum.buildLbq(executorProps.getQueueType(), executorProps.getQueueCapacity());
        return new Object[]{
                executorProps.getCorePoolSize(),
                executorProps.getMaximumPoolSize(),
                executorProps.getKeepAliveTime(),
                executorProps.getUnit(),
                blockingQueue,
                new NamedThreadFactory(executorProps.getThreadNamePrefix()),
                RejectedTypeEnum.buildRejectedHandler(executorProps.getRejectedHandlerType())
        };
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


}
