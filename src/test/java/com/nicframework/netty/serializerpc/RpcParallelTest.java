package com.nicframework.netty.serializerpc;

import cn.hutool.core.date.StopWatch;
import com.nicframework.netty.serializerpc.core.client.MessageSendExecutor;
import com.nicframework.netty.serializerpc.servicebean.CalcParallelRequestThread;
import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 17:01
 */
public class RpcParallelTest
{
    public static void parallelTask(MessageSendExecutor executor, int parallel, String serverAddress, RpcSerializeProtocol protocol) throws InterruptedException {
        //开始计时
        StopWatch sw = new StopWatch();
        sw.start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);

        for (int index = 0; index < parallel; index++) {
            CalcParallelRequestThread client = new CalcParallelRequestThread(executor, signal, finish, index);
            new Thread(client).start();
        }

        //10000个并发线程瞬间发起请求操作
        signal.countDown();
        finish.await();
        sw.stop();

        String tip = String.format("[%s] RPC调用总共耗时: [%s] 毫秒", protocol, sw.getTotalTimeMillis());
        System.out.println(tip);

    }

    //JDK本地序列化协议
    public static void JdkNativeParallelTask(MessageSendExecutor executor, int parallel) throws InterruptedException {
        String serverAddress = "127.0.0.1:7000";
        RpcSerializeProtocol protocol = RpcSerializeProtocol.JDKSERIALIZE;
        executor.setRpcServerLoader(serverAddress, protocol);
        RpcParallelTest.parallelTask(executor, parallel, serverAddress, protocol);
        TimeUnit.SECONDS.sleep(3);
    }

    //Kryo序列化协议
    public static void KryoParallelTask(MessageSendExecutor executor, int parallel) throws InterruptedException {
        String serverAddress = "127.0.0.1:7001";
        RpcSerializeProtocol protocol = RpcSerializeProtocol.KRYOSERIALIZE;
        executor.setRpcServerLoader(serverAddress, protocol);
        RpcParallelTest.parallelTask(executor, parallel, serverAddress, protocol);
        TimeUnit.SECONDS.sleep(3);
    }

    //Hessian序列化协议
    public static void HessianParallelTask(MessageSendExecutor executor, int parallel) throws InterruptedException {
        String serverAddress = "127.0.0.1:7002";
        RpcSerializeProtocol protocol = RpcSerializeProtocol.HESSIANSERIALIZE;
        executor.setRpcServerLoader(serverAddress, protocol);
        RpcParallelTest.parallelTask(executor, parallel, serverAddress, protocol);
        TimeUnit.SECONDS.sleep(3);
    }

    public static void main(String[] args) throws Exception {
        //并行度10000
        int parallel = 10000;
        MessageSendExecutor executor = new MessageSendExecutor();

        for (int i = 0; i < 5; i++) {
            JdkNativeParallelTask(executor, parallel);
            KryoParallelTask(executor, parallel);
            HessianParallelTask(executor, parallel);
            System.out.printf("nicNettyRpc Server 消息协议序列化第[%d]轮并发验证结束!\n\n", i);
        }

        executor.stop();
    }
}
