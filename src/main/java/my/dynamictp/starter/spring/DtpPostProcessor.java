package my.dynamictp.starter.spring;

import my.dynamictp.starter.properties.DtpExecutorProps;
import my.dynamictp.starter.support.DtpRegistry;
import my.dynamictp.starter.support.ExecutorWrapper;
import my.dynamictp.starter.thread.DtpExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

public class DtpPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (!(bean instanceof DtpExecutor)) {
            return bean;
        }
        registerDtp(bean);
        return bean;
    }
    private void registerDtp(Object bean){
        DtpExecutor dtpExecutor = (DtpExecutor) bean;
        DtpRegistry.registerExecutor(ExecutorWrapper.of(dtpExecutor), "beanPostProcessor");
    }
}
