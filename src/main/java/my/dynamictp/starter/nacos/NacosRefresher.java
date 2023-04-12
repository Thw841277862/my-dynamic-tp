package my.dynamictp.starter.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import lombok.extern.slf4j.Slf4j;
import my.dynamictp.starter.enums.ConfigFileTypeEnum;
import my.dynamictp.starter.properties.DtpProperties;
import my.dynamictp.starter.refresher.AbstractRefresher;
import my.dynamictp.starter.util.NacosUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.concurrent.Executor;


/**
 * NacosRefresher related
 *
 * @author yanhom
 * @since 1.0.0
 **/
@Slf4j
public class NacosRefresher extends AbstractRefresher implements InitializingBean, Listener {


    private ConfigFileTypeEnum configFileType;

    @NacosInjected
    private ConfigService configService;

    @Resource
    private Environment environment;



    @Override
    public void afterPropertiesSet() {

        DtpProperties.Nacos nacos = dtpProperties.getNacos();
        configFileType = NacosUtil.getConfigType(dtpProperties, ConfigFileTypeEnum.PROPERTIES);
        String dataId = NacosUtil.deduceDataId(nacos, environment, configFileType);
        String group = NacosUtil.getGroup(nacos, "DEFAULT_GROUP");

        try {
            configService.addListener(dataId, group, this);
            log.info("DynamicTp refresher, add listener success, dataId: {}, group: {}", dataId, group);
        } catch (NacosException e) {
            log.error("DynamicTp refresher, add listener error, dataId: {}, group: {}", dataId, group, e);
        }
    }


    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String content) {
        refresh(content, configFileType);
    }


}
