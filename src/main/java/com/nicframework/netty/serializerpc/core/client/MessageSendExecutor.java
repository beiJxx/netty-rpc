package com.nicframework.netty.serializerpc.core.client;

import com.google.common.reflect.Reflection;
import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:26
 */
public class MessageSendExecutor
{
    private final RpcServerLoader loader = RpcServerLoader.getInstance();

    public MessageSendExecutor() {
    }

    public void setRpcServerLoader(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        loader.load(serverAddress, serializeProtocol);
    }

    public void stop() {
        loader.unLoad();
    }

    public <T> T execute(Class<T> rpcInterface) {
        return Reflection.newProxy(rpcInterface, new MessageSendProxy<T>());
    }
}
