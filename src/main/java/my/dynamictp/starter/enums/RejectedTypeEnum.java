package my.dynamictp.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * RejectedTypeEnum related
 **/
@Slf4j
@Getter
@AllArgsConstructor
public enum RejectedTypeEnum {

    ABORT_POLICY("AbortPolicy"),

    CALLER_RUNS_POLICY("CallerRunsPolicy"),

    DISCARD_OLDEST_POLICY("DiscardOldestPolicy"),

    DISCARD_POLICY("DiscardPolicy");

    private final String name;


    public static RejectedExecutionHandler buildRejectedHandler(String name) {
        if (Objects.equals(name, ABORT_POLICY.getName())) {
            return new ThreadPoolExecutor.AbortPolicy();
        } else if (Objects.equals(name, CALLER_RUNS_POLICY.getName())) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        } else if (Objects.equals(name, DISCARD_OLDEST_POLICY.getName())) {
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        } else if (Objects.equals(name, DISCARD_POLICY.getName())) {
            return new ThreadPoolExecutor.DiscardPolicy();
        }
        Assert.notNull(null, "Cannot find specified RejectedExecutionHandler" + name);
        return null;
    }
}

