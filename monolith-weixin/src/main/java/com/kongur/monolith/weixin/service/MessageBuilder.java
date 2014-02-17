package com.kongur.monolith.weixin.service;

import com.kongur.monolith.weixin.domain.Message;


/**
 * 
 * 创建消息对象
 *
 *@author zhengwei
 *
 *@date 2014-2-14	
 *
 */
public interface MessageBuilder {

    Message build(String signature, String timestamp, String nonce, String echostr, String receivedMsg);
    
}

