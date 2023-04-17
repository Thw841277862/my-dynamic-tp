package my.dynamictp.starter.spring;

import my.dynamictp.starter.properties.DtpExecutorProps;
import my.dynamictp.starter.properties.DtpProperties;
import my.dynamictp.starter.support.DtpRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DtpProperties.class)
public class DtpBaseBeanConfiguration {

    @Bean
    public DtpPostProcessor dtpPostProcessor(){
        return new DtpPostProcessor();
    }

    @Bean
    public DtpRegistry dtpRegistry(DtpProperties properties) {
        return new DtpRegistry(properties);
    }
}
