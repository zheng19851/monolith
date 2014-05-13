package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;


/**
 * 
 * 关注事件消息
 *
 *@author zhengwei
 *
 *@date 2014-2-19	
 *
 */
public class SubscribeEventMessage extends EventMessage {

    public SubscribeEventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 2085631136417374320L;
    
    
   
}

