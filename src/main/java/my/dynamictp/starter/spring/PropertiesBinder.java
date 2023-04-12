package my.dynamictp.starter.spring;

import my.dynamictp.starter.properties.DtpProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;

import java.util.Map;

import static my.dynamictp.starter.constant.DynamicTpConst.PROPERTIES_PREFIX;

/**
 * 属性配置解析类
 */
public class PropertiesBinder {
    private PropertiesBinder() {
    }

    public static void bindDtpProperties(Environment environment, DtpProperties dtpProperties) {
        Binder binder = Binder.get(environment);
        ResolvableType resolvableType = ResolvableType.forClass(DtpProperties.class);
        Bindable<?> target = Bindable.of(resolvableType).withExistingValue(dtpProperties);
        binder.bind(PROPERTIES_PREFIX, target);
    }

    public static void bindDtpProperties(Map<?, Object> properties, DtpProperties dtpProperties) {
        ConfigurationPropertySource sources = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(sources);
        ResolvableType type = ResolvableType.forClass(DtpProperties.class);
        Bindable<?> target = Bindable.of(type).withExistingValue(dtpProperties);
        binder.bind(PROPERTIES_PREFIX, target);
    }
}
