package com.nicframework.netty.serializerpc.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Description:
 *
 * @author james
 * @date 2021/7/28 16:22
 */
public interface RpcSerialize
{
    void serialize(OutputStream output, Object object) throws IOException;

    Object deserialize(InputStream input) throws IOException;
}
