package com.kongur.monolith.im.weixin.domain;

import java.util.Map;

import com.kongur.monolith.im.domain.AbstractMessage;

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

    public DefaultMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

}
