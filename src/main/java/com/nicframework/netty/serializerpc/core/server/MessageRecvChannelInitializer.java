package com.nicframework.netty.serializerpc.core.server;

import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:39
 */
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private RpcSerializeProtocol rpcSerializeProtocol;

    private RpcRecvSerializeFrame rpcRecvSerializeFrame = null;

    public MessageRecvChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
        this.rpcSerializeProtocol = protocol;
        return this;
    }

    public MessageRecvChannelInitializer(Map<String, Object> handlerMap) {
        rpcRecvSerializeFrame = new RpcRecvSerializeFrame(handlerMap);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        rpcRecvSerializeFrame.select(rpcSerializeProtocol, pipeline);
    }
}
