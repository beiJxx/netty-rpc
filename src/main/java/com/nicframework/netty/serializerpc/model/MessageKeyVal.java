package com.nicframework.netty.serializerpc.model;

import java.util.Map;

/**
 * Description:
 * rpc服务映射容器
 *
 * @author james
 * @date 2021/7/28 14:17
 */
public class MessageKeyVal
{
    private Map<String, Object> messageKeyVal;

    public void setMessageKeyVal(Map<String, Object> messageKeyVal) {
        this.messageKeyVal = messageKeyVal;
    }

    public Map<String, Object> getMessageKeyVal() {
        return messageKeyVal;
    }
}
