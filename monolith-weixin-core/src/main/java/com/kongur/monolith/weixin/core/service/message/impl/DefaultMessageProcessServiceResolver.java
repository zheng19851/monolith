package com.kongur.monolith.weixin.core.service.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.kongur.monolith.weixin.core.common.OrderComparator;
import com.kongur.monolith.weixin.core.domain.message.Message;
import com.kongur.monolith.weixin.core.service.message.MessageProcessService;
import com.kongur.monolith.weixin.core.service.message.MessageProcessServiceResolver;

/**
 * 微信 消息处理服务创建工厂
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
// @Service("defaultMessageProcessServiceResolver")
public class DefaultMessageProcessServiceResolver implements MessageProcessServiceResolver {

    private final Logger                         log = Logger.getLogger(getClass());

    private List<MessageProcessService<Message>> messageProcessServices;

    /**
     * 默认的MessageProcessService，找不到匹配的service时，就默认用这个
     */
    // @Resource(name = "discardMessageProcessService")
    private MessageProcessService<Message>       defaultMessageProcessService;

    // @PostConstruct
    public void init() {
        if (this.messageProcessServices == null) {
            this.messageProcessServices = new ArrayList<MessageProcessService<Message>>();
        }

        Assert.notNull(this.defaultMessageProcessService, "the default MessageProcessService can not be null.");

        OrderComparator.sort(this.messageProcessServices);
    }

    @Override
    public MessageProcessService<Message> resolve(Message msg) {

        MessageProcessService<Message> service = null;

        for (MessageProcessService<Message> messageProcessService : this.messageProcessServices) {
            if (messageProcessService.supports(msg)) {
                if (log.isDebugEnabled()) {
                    log.debug("find MessageProcessService, name=" + messageProcessService.getClass().getSimpleName());
                }
                service = messageProcessService;
                break;
            }
        }

        if (service == null) {

            log.warn("can not find a MessageProcessService, so will use the default MessageProcessService="
                     + this.defaultMessageProcessService.getClass().getSimpleName());
            service = this.defaultMessageProcessService;
        }

        return service;
    }

    @Override
    public boolean addMessageProcessService(MessageProcessService<Message> service) {
        return this.messageProcessServices.add(service);
    }

    @Override
    public boolean removeMessageProcessService(MessageProcessService<Message> service) {
        return this.messageProcessServices.remove(service);
    }

    public MessageProcessService<Message> getDefaultMessageProcessService() {
        return defaultMessageProcessService;
    }

    public void setDefaultMessageProcessService(MessageProcessService<Message> defaultMessageProcessService) {
        this.defaultMessageProcessService = defaultMessageProcessService;
    }

    public void setMessageProcessServices(List<MessageProcessService<Message>> messageProcessServices) {
        this.messageProcessServices = messageProcessServices;
    }

    public List<MessageProcessService<Message>> getMessageProcessServices() {
        return messageProcessServices;
    }

}
