package com.shenfeng.yxw.znode.service;

import java.util.concurrent.TimeUnit;

/**
 * @Author yangxw
 * @Date 11/9/2020 上午9:19
 * @Description
 * @Version 1.0
 */
public abstract class BaseLockHandler<T> {
    private static final int DEFAULT_TIME_OUT = 200;
    // 加锁的资源路径
    private String path;

    public BaseLockHandler(String path) {
        this.path = path;
    }

    /**
     * 具体的业务实现逻辑，重写该方法
     *
     * @return
     */
    public abstract T handler();

    /**
     * 返回加锁的路径
     *
     * @return
     */
    public String getPath() {
        return this.path;
    }

    /**
     * 返回加锁的超时时间
     *
     * @return
     */
    public int getTimeOut() {
        return DEFAULT_TIME_OUT;
    }

    /**
     * 时间单位
     *
     * @return
     */
    public TimeUnit getTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}