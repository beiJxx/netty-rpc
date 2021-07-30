package com.nicframework.netty.serializerpc.core.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.nicframework.netty.serializerpc.common.Constants;
import com.nicframework.netty.serializerpc.core.RpcThreadPool;
import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description:
 * rpc服务器配置加载
 *
 * @author james
 * @date 2021/7/28 14:27
 */
public class RpcServerLoader
{
    private volatile static RpcServerLoader rpcServerLoader;
    //    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;

    //netty nio线程池
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private static ListeningExecutorService THREAD_POOL_EXECUTOR = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1));
    private MessageSendHandler messageSendHandler = null;

    //等待Netty服务端链路建立通知信号
    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    private RpcServerLoader() {
    }

    //并发双重锁定
    public static RpcServerLoader getInstance() {
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        String[] ipAddr = serverAddress.split(Constants.DELIMITER);
        if (ipAddr.length == 2) {
            String host = ipAddr[0];
            int port = Integer.parseInt(ipAddr[1]);
            final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);

            ListenableFuture<Boolean> listenableFuture = THREAD_POOL_EXECUTOR.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddr, serializeProtocol));

            //监听线程池异步的执行结果成功与否再决定是否唤醒全部的客户端RPC线程
            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>()
            {
                @Override
                public void onSuccess(Boolean result) {
                    lock.lock();
                    try {
                        if (null == messageSendHandler) {
                            handlerStatus.await();
                        }

                        //Futures异步回调，唤醒所有rpc等待线程
                        if (result && null != messageSendHandler) {
                            connectStatus.signalAll();
                        }
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(RpcServerLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                        lock.unlock();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, THREAD_POOL_EXECUTOR);
        }
    }

    public void setMessageSendHandler(MessageSendHandler messageInHandler) {
        lock.lock();
        try {
            this.messageSendHandler = messageInHandler;
            //唤醒所有等待客户端RPC线程
            handlerStatus.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
        lock.lock();
        try {
            //Netty服务端链路没有建立完毕之前，先挂起等待
            if (messageSendHandler == null) {
                connectStatus.await();
            }
            return messageSendHandler;
        }
        finally {
            lock.unlock();
        }
    }

    public void unLoad() {
        messageSendHandler.close();
        THREAD_POOL_EXECUTOR.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    //    public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
    //        this.serializeProtocol = serializeProtocol;
    //    }
}
