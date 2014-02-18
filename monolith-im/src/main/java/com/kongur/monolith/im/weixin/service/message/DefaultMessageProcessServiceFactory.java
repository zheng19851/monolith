package com.kongur.monolith.im.weixin.service.message;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.kongur.monolith.im.domain.message.Message;
import com.kongur.monolith.im.serivce.message.MessageProcessService;
import com.kongur.monolith.im.serivce.message.MessageProcessServiceFactory;

/**
 * 微信 消息处理服务创建工厂
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
// @Service("defaultMessageProcessServiceFactory")
public class DefaultMessageProcessServiceFactory implements MessageProcessServiceFactory {

    private final Logger                                             log                      = Logger.getLogger(getClass());

    /**
     * key=msgType
     */
    private Map<String/* msgType */, MessageProcessService<Message>> messageProcessServiceMap = null;

    /**
     * 默认的MessageProcessService，找不到匹配的service时，就默认用这个
     */
    @Resource(name = "discardMessageProcessService")
    private MessageProcessService<Message>                           defaultMessageProcessService;

    // @PostConstruct
    // public void init() {
    // if (this.messageProcessServiceMap == null) {
    // this.messageProcessServiceMap = new HashMap<String, MessageProcessService<Message>>();
    // }
    //
    // }

    @Override
    public MessageProcessService<Message> createMessageProcessService(String msgType) {

        MessageProcessService<Message> service = messageProcessServiceMap.get(msgType);

        if (service != null) {
            if (log.isDebugEnabled()) {
                log.debug("find MessageProcessService, name=" + service.getClass().getSimpleName());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("can not find any MessageProcessService, so will use the default MessageProcessService="
                          + this.defaultMessageProcessService.getClass().getSimpleName());
            }
            service = this.defaultMessageProcessService;
        }

        return service;
    }

    public Map<String, MessageProcessService<Message>> getMessageProcessServiceMap() {
        return messageProcessServiceMap;
    }

    public void setMessageProcessServiceMap(Map<String, MessageProcessService<Message>> messageProcessServiceMap) {
        this.messageProcessServiceMap = messageProcessServiceMap;
    }

    public MessageProcessService<Message> getDefaultMessageProcessService() {
        return defaultMessageProcessService;
    }

    public void setDefaultMessageProcessService(MessageProcessService<Message> defaultMessageProcessService) {
        this.defaultMessageProcessService = defaultMessageProcessService;
    }

}
