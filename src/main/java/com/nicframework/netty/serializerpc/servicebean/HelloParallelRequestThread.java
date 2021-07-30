package com.nicframework.netty.serializerpc.servicebean;

import com.nicframework.netty.serializerpc.core.client.MessageSendExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:46
 */
public class HelloParallelRequestThread implements Runnable
{
    private CountDownLatch signal;
    private CountDownLatch finish;
    private MessageSendExecutor executor;
    private int taskNumber = 0;

    public HelloParallelRequestThread(MessageSendExecutor executor, CountDownLatch signal, CountDownLatch finish, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            signal.await();

            IHello hello = executor.execute(IHello.class);
            String helloStr = hello.getHello(taskNumber + "");
            System.out.println("hello =====> " + helloStr);

            finish.countDown();
        }
        catch (InterruptedException ex) {
            Logger.getLogger(HelloParallelRequestThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
