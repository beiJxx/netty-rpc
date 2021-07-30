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
public class CalcParallelRequestThread implements Runnable
{
    private CountDownLatch signal;
    private CountDownLatch finish;
    private MessageSendExecutor executor;
    private int taskNumber = 0;

    public CalcParallelRequestThread(MessageSendExecutor executor, CountDownLatch signal, CountDownLatch finish, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            signal.await();

            ICalculate calc = executor.execute(ICalculate.class);
            int add = calc.add(taskNumber, taskNumber);
            //            System.out.println("calc add result:[" + add + "]");

            finish.countDown();
        }
        catch (InterruptedException ex) {
            Logger.getLogger(CalcParallelRequestThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
