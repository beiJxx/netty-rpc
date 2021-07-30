package com.nicframework.netty.serializerpc.core.client;

import com.google.common.reflect.AbstractInvocationHandler;
import com.nicframework.netty.serializerpc.model.MessageRequest;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:28
 */
public class MessageSendProxy<T> extends AbstractInvocationHandler
{
    @Override
    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParameters(args);

        MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
        MessageCallBack callBack = handler.sendRequest(request);
        return callBack.start();
    }
}
