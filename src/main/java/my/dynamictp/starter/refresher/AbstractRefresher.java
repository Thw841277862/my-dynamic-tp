package my.dynamictp.starter.refresher;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import my.dynamictp.starter.enums.ConfigFileTypeEnum;
import my.dynamictp.starter.event.RefreshEvent;
import my.dynamictp.starter.properties.DtpProperties;
import my.dynamictp.starter.spring.PropertiesBinder;
import my.dynamictp.starter.util.ConfigHandler;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * AbstractRefresher related
 *
 * @author yanhom
 * @since 1.0.0
 **/
@Slf4j
public abstract class AbstractRefresher implements Refresher, ApplicationContextAware {

    @Resource
    protected DtpProperties dtpProperties;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void refresh(String content, ConfigFileTypeEnum fileType) {

        if (StringUtils.isBlank(content) || Objects.isNull(fileType)) {
            log.warn("DynamicTp refresh, empty content or null fileType.");
            return;
        }

        try {
            ConfigHandler configHandler = ConfigHandler.getInstance();
            Map<Object, Object> properties = configHandler.parseConfig(content, fileType);
            doRefresh(properties);
        } catch (IOException e) {
            log.error("DynamicTp refresh error, content: {}, fileType: {}", content, fileType, e);
        }
    }

    protected void doRefresh(Map<Object, Object> properties) {
        if (MapUtils.isEmpty(properties)) {
            log.warn("DynamicTp refresh, empty properties.");
            return;
        }
        PropertiesBinder.bindDtpProperties(properties, dtpProperties);
        doRefresh(dtpProperties);
    }

    protected void doRefresh(DtpProperties dtpProperties) {
        DtpRegistry.refresh(dtpProperties);
        publishEvent(dtpProperties);
    }

    private void publishEvent(DtpProperties dtpProperties) {
        RefreshEvent event = new RefreshEvent(this, dtpProperties);
        context.publishEvent(event);
    }
}
