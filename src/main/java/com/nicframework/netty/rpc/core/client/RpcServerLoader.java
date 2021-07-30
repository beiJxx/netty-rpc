package com.nicframework.netty.rpc.core.client;

import com.nicframework.netty.rpc.common.Constants;
import com.nicframework.netty.rpc.core.RpcThreadPool;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    //netty nio线程池
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
    private MessageSendHandler messageSendHandler = null;

    //等待Netty服务端链路建立通知信号
    private Lock lock = new ReentrantLock();
    private Condition signal = lock.newCondition();

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

    public void load(String serverAddress) {
        String[] ipAddr = serverAddress.split(Constants.DELIMITER);
        if (ipAddr.length == 2) {
            String host = ipAddr[0];
            int port = Integer.parseInt(ipAddr[1]);
            final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);

            threadPoolExecutor.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddr, this));
        }
    }

    public void setMessageSendHandler(MessageSendHandler messageInHandler) {
        lock.lock();
        try {
            this.messageSendHandler = messageInHandler;
            //唤醒所有等待客户端RPC线程
            signal.signalAll();
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
                signal.await();
            }
            return messageSendHandler;
        }
        finally {
            lock.unlock();
        }
    }

    public void unLoad() {
        messageSendHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

}
