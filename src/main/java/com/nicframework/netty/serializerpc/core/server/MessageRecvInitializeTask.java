package com.nicframework.netty.serializerpc.core.server;

import com.nicframework.netty.serializerpc.model.MessageRequest;
import com.nicframework.netty.serializerpc.model.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.reflect.MethodUtils;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:40
 */
public class MessageRecvInitializeTask implements Callable<Boolean>
{

    private MessageRequest request = null;
    private MessageResponse response = null;
    private Map<String, Object> handlerMap = null;
    private ChannelHandlerContext ctx = null;

    MessageRecvInitializeTask(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.handlerMap = handlerMap;
        this.ctx = ctx;
    }

    private Object reflect(MessageRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();

        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }

    @Override
    public Boolean call() throws Exception {
        response.setMessageId(request.getMessageId());
        try {
            Object result = reflect(request);
            response.setResult(result);
            return Boolean.TRUE;
        }
        catch (Throwable t) {
            response.setError(t.toString());
            t.printStackTrace();
            System.err.println("nicNettyRpc Server invoke error!\n");
            return Boolean.FALSE;
        }
    }
}
