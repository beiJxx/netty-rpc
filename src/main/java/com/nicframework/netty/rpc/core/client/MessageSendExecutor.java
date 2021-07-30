package com.nicframework.netty.rpc.core.client;

import java.lang.reflect.Proxy;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:26
 */
public class MessageSendExecutor
{
    private RpcServerLoader loader = RpcServerLoader.getInstance();

    public MessageSendExecutor(String serverAddress) {
        loader.load(serverAddress);
    }

    public void stop() {
        loader.unLoad();
    }

    public <T> T execute(Class<T> rpcInterface) {
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class<?>[] {rpcInterface}, new MessageSendProxy<T>(rpcInterface)
        );
    }
}
