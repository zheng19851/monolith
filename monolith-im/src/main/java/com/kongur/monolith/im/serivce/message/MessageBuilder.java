package com.kongur.monolith.im.serivce.message;

import javax.servlet.http.HttpServletRequest;

import com.kongur.monolith.im.domain.message.Message;


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

    /**
     * 组装消息对象
     * 
     * @param req
     * @return
     */
    Message build(HttpServletRequest req);
    
}

