package com.nicframework.netty.serializerpc.support.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.nicframework.netty.serializerpc.model.MessageRequest;
import com.nicframework.netty.serializerpc.model.MessageResponse;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * Description:
 * Kryo对象池工厂
 *
 * @author james
 * @date 2021/7/27 9:09
 */
public class KryoPoolFactory
{

    private static KryoPoolFactory kryoPoolFactory = null;

    private KryoFactory factory = () -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        //把已知的结构注册到kryo注册器里面，提高序列化/反序列化的效率
        kryo.register(MessageRequest.class);
        kryo.register(MessageResponse.class);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    };

    private KryoPool pool = new KryoPool.Builder(factory).build();

    public KryoPoolFactory() {
    }

    public static KryoPool getKryoPoolInstance() {
        if (null == kryoPoolFactory) {
            synchronized (KryoPoolFactory.class) {
                if (null == kryoPoolFactory) {
                    kryoPoolFactory = new KryoPoolFactory();
                }
            }
        }
        return kryoPoolFactory.getPool();
    }

    public KryoPool getPool() {
        return pool;
    }
}
