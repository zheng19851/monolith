package com.kongur.monolith.im.serivce.message;

import com.kongur.monolith.im.domain.message.Message;


/**
 * 消息处理器创建工厂
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface MessageProcessServiceFactory {

    /**
     * 创建对应的消息服务
     * 
     * @param msgType
     * @return
     */
    MessageProcessService<Message> createMessageProcessService(String msgType);

}
