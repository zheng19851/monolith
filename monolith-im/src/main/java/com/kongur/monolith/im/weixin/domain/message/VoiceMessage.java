package com.kongur.monolith.im.weixin.domain.message;

import com.kongur.monolith.im.domain.AbstractMessage;


/**
 *语言类型的消息
 *
 *@author zhengwei
 *
 *@date 2014-2-14	
 *
 */
public class VoiceMessage extends AbstractMessage {

    public VoiceMessage(String signature, String timestamp, String nonce) {
        super(signature, timestamp, nonce);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -4930721385706842976L;

}

