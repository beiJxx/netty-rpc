package com.nicframework.netty.serializerpc.core.client;

import com.nicframework.netty.serializerpc.common.Constants;
import com.nicframework.netty.serializerpc.support.RpcSerializeFrame;
import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;
import com.nicframework.netty.serializerpc.support.hessian.HessianCodecUtil;
import com.nicframework.netty.serializerpc.support.hessian.HessianDecoder;
import com.nicframework.netty.serializerpc.support.hessian.HessianEncoder;
import com.nicframework.netty.serializerpc.support.kryo.KryoCodecUtil;
import com.nicframework.netty.serializerpc.support.kryo.KryoDecoder;
import com.nicframework.netty.serializerpc.support.kryo.KryoEncoder;
import com.nicframework.netty.serializerpc.support.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:33
 */
public class RpcSendSerializeFrame implements RpcSerializeFrame
{

    //后续可以优化成通过spring ioc方式注入
    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE:
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Constants.MESSAGE_LENGTH, 0, Constants.MESSAGE_LENGTH));
                pipeline.addLast(new LengthFieldPrepender(Constants.MESSAGE_LENGTH));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                pipeline.addLast(new MessageSendHandler());
                break;

            case KRYOSERIALIZE:
                KryoCodecUtil kryoCodecUtil = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
                pipeline.addLast(new KryoEncoder(kryoCodecUtil));
                pipeline.addLast(new KryoDecoder(kryoCodecUtil));
                pipeline.addLast(new MessageSendHandler());
                break;

            case HESSIANSERIALIZE:
                HessianCodecUtil hessianCodecUtil = new HessianCodecUtil();
                pipeline.addLast(new HessianEncoder(hessianCodecUtil));
                pipeline.addLast(new HessianDecoder(hessianCodecUtil));
                pipeline.addLast(new MessageSendHandler());
                break;
            default:
                break;
        }
    }
}
