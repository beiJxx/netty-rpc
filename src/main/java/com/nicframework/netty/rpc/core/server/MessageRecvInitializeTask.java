package com.nicframework.netty.rpc.core.server;

import com.nicframework.netty.rpc.model.MessageRequest;
import com.nicframework.netty.rpc.model.MessageResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.reflect.MethodUtils;

import java.util.Map;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:40
 */
public class MessageRecvInitializeTask implements Runnable
{

    private MessageRequest request = null;
    private MessageResponse response = null;
    private Map<String, Object> handlerMap = null;
    private ChannelHandlerContext ctx = null;

    public MessageResponse getResponse() {
        return response;
    }

    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    MessageRecvInitializeTask(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.handlerMap = handlerMap;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        response.setMessageId(request.getMessageId());
        try {
            Object result = reflect(request);
            response.setResult(result);
        }
        catch (Throwable t) {
            response.setError(t.toString());
            t.printStackTrace();
            System.err.println("nicNettyRpc Server invoke error!\n");
        }

        ctx.writeAndFlush(response)
                .addListener(new ChannelFutureListener()
                {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("nicNettyRpc Server Send message-id respone:" + request.getMessageId());
                    }
                });
    }

    private Object reflect(MessageRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }
}
