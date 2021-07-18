package com.vimdream.htool.thread.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Title: DelayTask
 * @Author vimdream
 * @ProjectName htool
 * @Date 2021/7/18 16:37
 */
public class DelayTask implements Delayed {

    private String taskName;
    private long timeout;
    private long futureTime;
    private TimeUnit unit;
    private Runnable task;

    @Override
    public long getDelay(TimeUnit unit) {
        return futureTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(getDelay(null), o.getDelay(null));
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getFutureTime() {
        return futureTime;
    }

    public void setFutureTime(long futureTime) {
        this.futureTime = futureTime;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }
}
