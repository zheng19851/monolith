package com.kongur.monolith.weixin.domain;

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

}
