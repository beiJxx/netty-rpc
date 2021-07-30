package com.nicframework.netty.serializerpc.core.server;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.nicframework.netty.serializerpc.common.Constants;
import com.nicframework.netty.serializerpc.core.NamedThreadFactory;
import com.nicframework.netty.serializerpc.core.RpcThreadPool;
import com.nicframework.netty.serializerpc.model.MessageKeyVal;
import com.nicframework.netty.serializerpc.model.MessageRequest;
import com.nicframework.netty.serializerpc.model.MessageResponse;
import com.nicframework.netty.serializerpc.support.RpcSerializeProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 14:36
 */
public class MessageRecvExecutor implements ApplicationContextAware, InitializingBean
{
    private final String serverAddress;
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;

    private final Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

    private static ListeningExecutorService threadPoolExecutor;

    public MessageRecvExecutor(String serverAddress, String serializeProtocol) {
        this.serverAddress = serverAddress;
        this.serializeProtocol = Enum.valueOf(RpcSerializeProtocol.class, serializeProtocol);
    }

    public static void submit(Callable<Boolean> task, ChannelHandlerContext ctx, MessageRequest request, MessageResponse response) {
        if (threadPoolExecutor == null) {
            synchronized (MessageRecvExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1));
                }
            }
        }
        ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(task);
        //Netty服务端把计算结果异步返回
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>()
        {
            @Override
            public void onSuccess(Boolean result) {
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener()
                {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("nicNettyRpc Server Send message-id respone:" + request.getMessageId());
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExecutor);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        try {
            MessageKeyVal keyVal = (MessageKeyVal) ctx.getBean(Class.forName("com.nicframework.netty.serializerpc.model.MessageKeyVal"));
            Map<String, Object> rpcServiceObject = keyVal.getMessageKeyVal();
            Iterator<Map.Entry<String, Object>> iterator = rpcServiceObject.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                handlerMap.put(next.getKey(), next.getValue());
            }
        }
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MessageRecvExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //netty的线程池模型设置成主从线程池模式，这样可以应对高并发请求
        //当然netty还支持单线程、多线程网络IO模型，可以根据业务需求灵活配置
        ThreadFactory threadRpcFactory = new NamedThreadFactory("nicNettyRpc ThreadFactory");

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(threadRpcFactory);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new MessageRecvChannelInitializer(handlerMap).buildRpcSerializeProtocol(serializeProtocol));

            String[] ipAddr = serverAddress.split(Constants.DELIMITER);

            if (ipAddr.length == 2) {
                String host = ipAddr[0];
                int port = Integer.parseInt(ipAddr[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                System.out.printf("nicNettyRpc Server start success ip:%s port:%d\n", host, port);
                future.channel().closeFuture().sync();
            }
            else {
                System.out.println("nicNettyRpc Server start fail!\n");
            }
        }
        finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
