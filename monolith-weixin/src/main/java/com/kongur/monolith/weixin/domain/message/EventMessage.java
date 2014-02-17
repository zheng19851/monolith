package com.kongur.monolith.weixin.domain.message;

import com.kongur.monolith.weixin.domain.AbstractMessage;


/**
 *事件推送的消息
 *
 *@author zhengwei
 *
 *@date 2014-2-14	
 *
 */
public class EventMessage extends AbstractMessage {

    public EventMessage(String signature, String timestamp, String nonce) {
        super(signature, timestamp, nonce);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 910441650557994025L;

}

