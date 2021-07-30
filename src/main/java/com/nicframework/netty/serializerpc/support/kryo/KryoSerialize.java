package com.nicframework.netty.serializerpc.support.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.nicframework.netty.serializerpc.support.RpcSerialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Description:
 * Kryo序列化/反序列化实现
 *
 * @author james
 * @date 2021/7/27 8:56
 */
public class KryoSerialize implements RpcSerialize
{

    private KryoPool pool = null;

    public KryoSerialize(KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void serialize(OutputStream outputStream, Object object) throws IOException {
        Kryo kryo = pool.borrow();
        Output output = new Output(outputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
        pool.release(kryo);
    }

    @Override
    public Object deserialize(InputStream inputStream) throws IOException {
        Kryo kryo = pool.borrow();
        Input input = new Input(inputStream);
        Object result = kryo.readClassAndObject(input);
        input.close();
        pool.release(kryo);
        return result;
    }
}
