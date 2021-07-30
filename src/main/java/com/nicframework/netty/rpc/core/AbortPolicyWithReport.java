package com.nicframework.netty.rpc.core;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:
 * 线程池拒绝策略
 *
 * @author james
 * @date 2021/7/28 14:25
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy
{

    private final String threadPoolName;

    public AbortPolicyWithReport(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("nicNettyRpcServer["
                        + " ThreadPool Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]",
                threadPoolName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
        System.out.println(msg);
        throw new RejectedExecutionException(msg);
    }
}
