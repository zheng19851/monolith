package com.kongur.monolith.weixin.domain;

import java.util.Map;

/**
 * 接收到的消息
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface Message {

    /**
     * 返回消息类型
     * 
     * @return
     */
    String getMsgType();

    /**
     * 加密签名
     * 
     * @return
     */
    String getSignature();

    /**
     * 时间戳
     * 
     * @return
     */
    String getTimestamp();

    /**
     * 随机数
     * 
     * @return
     */
    String getNonce();

    /**
     * 消息业务数据
     * 
     * @return
     */
    Map<String, Object> getParams();

    /**
     * 获取业务数据
     * 
     * @param key
     * @return
     */
    String getParamString(String key);

    /**
     * 获取业务数据
     * 
     * @param key
     * @return
     */
    Object getParam(String key);

}
