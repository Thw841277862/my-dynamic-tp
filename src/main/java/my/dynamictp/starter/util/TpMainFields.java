package my.dynamictp.starter.util;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 *
 **/
@Data
public class TpMainFields {

    private static final List<Field> FIELD_NAMES;

    static {
        FIELD_NAMES = Arrays.asList(TpMainFields.class.getDeclaredFields());
    }

    private String threadPoolName;

    private int corePoolSize;

    private int maxPoolSize;

    private long keepAliveTime;

    private String queueType;

    private int queueCapacity;

    private String rejectType;

    private boolean allowCoreThreadTimeOut;

    public static List<Field> getMainFields() {
        return FIELD_NAMES;
    }
}
