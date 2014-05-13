package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;

import com.kongur.monolith.weixin.core.domain.message.AbstractMessage;

/**
 * 事件推送的消息
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public class EventMessage extends AbstractMessage {

    public EventMessage(String signature, String timestamp, String nonce) {
        super(signature, timestamp, nonce);
    }

    public EventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 910441650557994025L;

    public String getEventType() {
        return this.getString("Event");
    }

}
