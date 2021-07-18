package com.vimdream.htool.thread.delay.queue;

import com.vimdream.htool.thread.delay.DelayProcessor;
import com.vimdream.htool.thread.delay.DelayTask;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Title: DefaultDelayQueueProcessor
 * @Author vimdream
 * @ProjectName htool
 * @Date 2021/7/18 16:42
 */
public class DefaultDelayQueueProcessor implements DelayProcessor {

    /**
     * selector poll()默认超时时间
     */
    private static final int DEFAULT_SELECTOR_INTERVAL = 5;

    /**
     * selector poll element timeout.
     */
    private int selectorInterval = DEFAULT_SELECTOR_INTERVAL;

    private ExecutorService executor;

    private DelayQueue<DelayTask> delayQueue;

    /**
     * 正在关闭
     */
    private volatile boolean shutting;

    /**
     * 已关闭
     */
    private volatile boolean shutdown;

    public DefaultDelayQueueProcessor(ExecutorService executor) {
        this(executor, DEFAULT_SELECTOR_INTERVAL);
    }

    public DefaultDelayQueueProcessor(ExecutorService executor, int selectorInterval) {
        this.selectorInterval = selectorInterval;
        this.shutting = false;
        this.shutdown = false;
        this.executor = executor;
        this.delayQueue = new DelayQueue<>();
        this.executor.execute(() -> {
            while (true) {
                try {
                    DelayTask task = delayQueue.poll(selectorInterval, TimeUnit.MILLISECONDS);
                    if (task == null) {
                        if (shutting && delayQueue.size() == 0) {
                            shutdown = true;
                            break;
                        }
                        continue;
                    }
                    executor.execute(task.getTask());
                } catch (InterruptedException e) {
                    shutdown = true;
                    System.out.println("selector is closed!");
                }
            }
        });
    }

    @Override
    public void process(DelayTask task) {
        if (this.shutting) {
            throw new IllegalStateException("DefaultDelayQueueProcessor is closed!");
        }
        delayQueue.put(task);
    }

    @Override
    public void waitComplete() {
        this.shutting = true;
        while (true) {
            if (shutdown) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.executor.shutdown();
        try {
            this.executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
