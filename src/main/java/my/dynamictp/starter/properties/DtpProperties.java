package my.dynamictp.starter.properties;

import lombok.Data;
import my.dynamictp.starter.constant.DynamicTpConst;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 动态线程池基础配置类
 *
 * @author thw
 */
@Data
//@ConfigurationProperties(prefix = DynamicTpConst.PROPERTIES_PREFIX);

public class DtpProperties {
    /**
     * 默认开启 动态线程池的启用
     */
    private boolean enabled = true;
    /**
     * 配置文件的默认类型
     */
    private String configType = "yaml";

    /**
     * 线程池配置 ThreadPoolExecutor configs.
     */
    private List<DtpExecutorProps> executors;

    private Nacos nacos;


    /**
     * 指定线程池的配置文件信息
     */
    @Data
    public static class Nacos {
        private String dataId;

        private String group;

        private String namespace;
    }
}
