package com.nicframework.netty.rpc.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * rpc线程池封装
 *
 * @author james
 * @date 2021/7/28 14:22
 */
public class RpcThreadPool
{
    //独立出线程池主要是为了应对复杂耗I/O操作的业务，不阻塞netty的handler线程而引入
    //当然如果业务足够简单，把处理逻辑写入netty的handler（ChannelInboundHandlerAdapter）也未尝不可
    public static Executor getExecutor(int threads, int queues) {
        String threadPoolName = "nicNettyRpcThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>()
                        : (queues < 0 ? new LinkedBlockingQueue<Runnable>()
                        : new LinkedBlockingQueue<Runnable>(queues)),
                new NamedThreadFactory(threadPoolName, true), new AbortPolicyWithReport(threadPoolName));
    }
}
