package my.dynamictp.starter.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用动态线程池开关
 *
 * @author yanhom
 * @since 1.0.4
 **/
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DtpConfigurationSelector.class)
public @interface EnableDynamicTp {
}
