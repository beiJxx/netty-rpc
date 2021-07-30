package com.nicframework.netty.serializerpc.core.client;

import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel>
{

    private RpcSerializeProtocol rpcSerializeProtocol;

    private RpcSendSerializeFrame rpcSendSerializeFrame = new RpcSendSerializeFrame();

    public MessageSendChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
        this.rpcSerializeProtocol = protocol;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        rpcSendSerializeFrame.select(rpcSerializeProtocol, pipeline);
    }
}
