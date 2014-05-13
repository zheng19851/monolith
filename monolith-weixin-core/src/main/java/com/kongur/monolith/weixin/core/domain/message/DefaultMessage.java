package com.kongur.monolith.weixin.core.domain.message;

import java.util.Map;

/**
 * 默认的消息对象
 * 
 * @author zhengwei
 * @date 2014-2-17
 */
public class DefaultMessage extends AbstractMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -3468433657850979829L;

    /**
     * 默认的消息对象
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public DefaultMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

}
