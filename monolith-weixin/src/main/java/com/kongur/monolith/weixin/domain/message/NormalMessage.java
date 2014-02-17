package com.kongur.monolith.weixin.domain.message;

import com.kongur.monolith.weixin.domain.AbstractMessage;


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

    /**
     * 
     */
    private static final long serialVersionUID = 6939471631141552306L;

}

