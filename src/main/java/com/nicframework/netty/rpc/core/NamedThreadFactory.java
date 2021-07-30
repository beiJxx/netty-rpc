package com.nicframework.netty.rpc.core;

import lombok.Data;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * 线程工厂
 *
 * @author james
 * @date 2021/7/28 14:17
 */
@Data
public class NamedThreadFactory implements ThreadFactory
{
    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemonThread;

    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("nicNettyRpc-threadPool-" + THREAD_NUMBER.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemonThread) {
        this.prefix = prefix + "-thread-";
        this.daemonThread = daemonThread;
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (null == securityManager) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + mThreadNum.getAndIncrement();
        Thread thread = new Thread(threadGroup, runnable, name, 0);
        thread.setDaemon(daemonThread);
        return thread;
    }
}
