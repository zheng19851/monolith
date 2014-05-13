package com.kongur.monolith.weixin.core.service.message;

import javax.servlet.http.HttpServletRequest;

import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * 创建消息对象
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface MessageBuilder {

    /**
     * 组装消息对象
     * 
     * @param req
     * @return
     */
    Message build(HttpServletRequest req);

    /**
     * 组装消息对象
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param req
     * @return
     */
    Message build(String signature, String timestamp, String nonce, String echostr, HttpServletRequest req);

}
