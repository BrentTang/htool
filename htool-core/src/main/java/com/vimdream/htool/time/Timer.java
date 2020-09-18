package com.vimdream.htool.time;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Timer
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/8/11 13:56
 */
public class Timer {

    private long start;

    private long lastStart;

    private List<Number> points;

    public Timer() {
        this.start = System.currentTimeMillis();
        this.lastStart = this.start;
        this.points = new ArrayList<>();
    }

    public void cut(Unit unit, boolean compareToStart) {
        long lastStart = System.currentTimeMillis();
        points.add(formatUnit(lastStart - (compareToStart ? this.start : this.lastStart), unit));
        this.lastStart = lastStart;
    }

    public Number interval(Unit unit, boolean compareToStart) {
        long lastStart = System.currentTimeMillis();
        Number interval = formatUnit(lastStart - (compareToStart ? this.start : this.lastStart), unit);
        this.lastStart = lastStart;
        return interval;
    }

    public Number formatUnit(long time, Unit unit) {
        switch (unit) {
            case MILLISECOND:
                return time;
            case SECOND:
                return (double) time / (double) 1000;
            default:
                return 0;
        }
    }

    public List<Number> getPoints() {
        return points;
    }

    public enum Unit{
        MILLISECOND, SECOND
    }

}
