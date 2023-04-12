package my.dynamictp.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 队列类型枚举
 */
@Getter
@AllArgsConstructor
public enum QueueTypeEnum {
    ARRAY_BLOCKING_QUEUE(1, "ArrayBlockingQueue"),

    LINKED_BLOCKING_QUEUE(2, "LinkedBlockingQueue");

    private final Integer code;
    private final String name;

    public static BlockingQueue<Runnable> buildLbq(String name, int capacity) {
        BlockingQueue<Runnable> blockingQueue = null;
        if (Objects.equals(name, ARRAY_BLOCKING_QUEUE.getName())) {
            blockingQueue = new ArrayBlockingQueue<>(capacity);
        } else if (Objects.equals(name, LINKED_BLOCKING_QUEUE.getName())) {
            blockingQueue = new LinkedBlockingQueue<>(capacity);
        }
        if (null != blockingQueue) {
            return blockingQueue;
        }
        Assert.notNull(blockingQueue, "Cannot find specified BlockingQueue" + name);
        return null;
    }
}
