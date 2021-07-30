package com.nicframework.netty.rpc.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:33
 */
public class MessageSendInitializeTask implements Runnable
{
    private EventLoopGroup eventLoopGroup = null;
    private InetSocketAddress serverAddress = null;
    private RpcServerLoader loader = null;

    public MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcServerLoader loader) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress = serverAddress;
        this.loader = loader;
    }

    @Override
    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new MessageSendChannelInitializer());

        ChannelFuture channelFuture = bootstrap.connect(serverAddress);
        channelFuture.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                    loader.setMessageSendHandler(handler);
                }
            }
        });
    }
}
