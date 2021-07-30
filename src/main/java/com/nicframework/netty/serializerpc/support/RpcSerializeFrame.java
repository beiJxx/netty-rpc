package com.nicframework.netty.serializerpc.support;

import io.netty.channel.ChannelPipeline;

/**
 * Description:
 * RPC消息序序列化协议选择器接口
 *
 * @author james
 * @date 2021/7/28 16:22
 */
public interface RpcSerializeFrame
{
    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}
