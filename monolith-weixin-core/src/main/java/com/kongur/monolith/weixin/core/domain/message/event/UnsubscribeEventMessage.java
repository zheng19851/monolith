package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;

/**
 * 取消关注消息
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class UnsubscribeEventMessage extends EventMessage {

    /**
     * 取消关注消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public UnsubscribeEventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 6931700961230934989L;

}
