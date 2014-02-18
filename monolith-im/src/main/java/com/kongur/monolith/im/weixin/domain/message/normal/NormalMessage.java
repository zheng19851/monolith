package com.kongur.monolith.im.weixin.domain.message.normal;

import java.util.Map;

import com.kongur.monolith.im.domain.AbstractMessage;


/**
 *普通的消息类型
 *
 *@author zhengwei
 *
 *@date 2014-2-14	
 *
 */
public class NormalMessage extends AbstractMessage {

    public NormalMessage(String signature, String timestamp, String nonce) {
        super(signature, timestamp, nonce);
    }
    
    public NormalMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 6939471631141552306L;

}

