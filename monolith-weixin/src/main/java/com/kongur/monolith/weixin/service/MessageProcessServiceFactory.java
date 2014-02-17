package com.kongur.monolith.weixin.service;


/**
 * 消息处理器创建工厂
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface MessageProcessServiceFactory {

    MessageProcessService createMessageProcessService(String msgType);

}
