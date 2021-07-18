package com.vimdream.htool.thread.delay;

/**
 * @Title: DelayProcessor
 * @Author vimdream
 * @ProjectName htool
 * @Date 2021/7/18 16:36
 */
public interface DelayProcessor {

    /**
     * 将延迟任务添加至延迟队列待执行
     * @param task
     */
    void process(DelayTask task);

    /**
     * 关闭延迟执行器，并同步等待所有延迟任务执行完毕
     */
    void waitComplete();

}
