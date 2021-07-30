package com.nicframework.netty.rpc.core.client;

import com.nicframework.netty.rpc.common.Constants;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:34
 */
public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel>
{

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
        //的初始化参数即为super(maxObjectSize, 0, 4, 0, 4);
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Constants.MESSAGE_LENGTH, 0, Constants.MESSAGE_LENGTH));
        //利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
        pipeline.addLast(new LengthFieldPrepender(Constants.MESSAGE_LENGTH));
        pipeline.addLast(new ObjectEncoder());
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        pipeline.addLast(new MessageSendHandler());
    }
}
