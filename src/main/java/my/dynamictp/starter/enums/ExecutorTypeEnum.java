package my.dynamictp.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import my.dynamictp.starter.thread.DtpExecutor;

/**
 * 线程池执行器枚举
 */
@Getter
@AllArgsConstructor
public enum ExecutorTypeEnum {

    BASE("base", DtpExecutor.class);

    private final String name;
    private final Class<?> clazz;

    public static Class<?> getClass(String name) {
        return BASE.getClass();
    }

}
