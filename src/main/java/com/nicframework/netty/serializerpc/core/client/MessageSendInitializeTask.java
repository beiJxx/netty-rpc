package com.nicframework.netty.serializerpc.core.client;

import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:33
 */
@Data
public class MessageSendInitializeTask implements Callable<Boolean>
{
    private EventLoopGroup eventLoopGroup = null;
    private InetSocketAddress serverAddress = null;
    private RpcSerializeProtocol protocol;

    public MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcSerializeProtocol protocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress = serverAddress;
        this.protocol = protocol;
    }

    @Override
    public Boolean call() throws Exception {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new MessageSendChannelInitializer().buildRpcSerializeProtocol(protocol));

        ChannelFuture channelFuture = b.connect(serverAddress);
        channelFuture.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                    RpcServerLoader.getInstance().setMessageSendHandler(handler);
                }
            }
        });
        return Boolean.TRUE;
    }
}
