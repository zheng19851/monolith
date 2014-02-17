package com.kongur.monolith.weixin.service.impl;

import org.springframework.stereotype.Service;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.domain.Message;
import com.kongur.monolith.weixin.domain.message.DeveloperValidateMessage;
import com.kongur.monolith.weixin.service.MessageBuilder;


/**
 * 
 * 创建微信消息对象
 *
 *@author zhengwei
 *
 *@date 2014-2-14	
 *
 */
@Service("defaultMessageBuilder")
public class DefaultMessageBuilder implements MessageBuilder {

    @Override
    public Message build(String signature, String timestamp, String nonce, String echostr, String receivedMsg) {
        
        Message msg = null;

        if (StringUtil.isNotBlank(echostr)) {
            msg = new DeveloperValidateMessage(signature, timestamp, nonce, echostr);
        }

        return msg;
    }

}

