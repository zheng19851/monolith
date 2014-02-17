package com.kongur.monolith.weixin.service;

import javax.servlet.http.HttpServletRequest;

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

    Message build(HttpServletRequest req);
    
}

