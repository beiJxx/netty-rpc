package com.nicframework.netty.serializerpc.support.hessian;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Description:
 * Hessian序列化/反序列化对象工厂池
 *
 * @author james
 * @date 2021/7/28 16:27
 */
public class HessianSerializeFactory extends BasePooledObjectFactory<HessianSerialize>
{

    @Override
    public HessianSerialize create() throws Exception {
        return createHessian();
    }

    @Override
    public PooledObject<HessianSerialize> wrap(HessianSerialize hessian) {
        return new DefaultPooledObject<HessianSerialize>(hessian);
    }

    private HessianSerialize createHessian() {
        return new HessianSerialize();
    }
}
