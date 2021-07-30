package com.nicframework.netty.rpc.core.client;

import com.nicframework.netty.rpc.model.MessageRequest;
import com.nicframework.netty.rpc.model.MessageResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:30
 */
public class MessageCallBack
{
    private MessageRequest request;
    private MessageResponse response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public MessageCallBack(MessageRequest request) {
        this.request = request;
    }

    public Object start() throws InterruptedException {
        lock.lock();
        try {
            //设定一下超时时间，rpc服务器太久没有相应的话，就默认返回空吧。
            finish.await(10 * 1000, TimeUnit.MILLISECONDS);
            return null == response ? response : response.getResult();
        }
        finally {
            lock.unlock();
        }
    }

    public void over(MessageResponse reponse) {
        lock.lock();
        try {
            finish.signal();
            this.response = reponse;
        }
        finally {
            lock.unlock();
        }
    }
}
