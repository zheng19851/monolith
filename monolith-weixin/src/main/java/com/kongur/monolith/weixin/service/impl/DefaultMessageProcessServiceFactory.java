package com.kongur.monolith.weixin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kongur.monolith.weixin.domain.Message;
import com.kongur.monolith.weixin.domain.message.DeveloperValidateMessage;
import com.kongur.monolith.weixin.service.DiscardMessageProcessService;
import com.kongur.monolith.weixin.service.MessageProcessService;
import com.kongur.monolith.weixin.service.MessageProcessServiceFactory;

/**
 * 微信 消息处理服务创建工厂
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("defaultMessageProcessServiceFactory")
public class DefaultMessageProcessServiceFactory implements MessageProcessServiceFactory {

    private final Logger                                             log                      = Logger.getLogger(getClass());

    /**
     * key=msgType
     */
    private Map<String/* msgType */, MessageProcessService<Message>> messageProcessServiceMap = null;

    @Resource(name = "developerValidateMessageProcessService")
    private MessageProcessService<Message>                   developerValidateMessageProcessService;

    @Autowired
    private DiscardMessageProcessService                             discardMessageProcessService;
    
    @PostConstruct
    public void init() {
        if(this.messageProcessServiceMap == null) {
            this.messageProcessServiceMap = new HashMap<String, MessageProcessService<Message>>();
        }
        
        // 验证开发者消息
        this.messageProcessServiceMap.put(DeveloperValidateMessage.DEVELOPER_VALIDATE_MESSAGE_TYPE, developerValidateMessageProcessService);
    }

    @Override
    public MessageProcessService createMessageProcessService(String msgType) {

        MessageProcessService service = null;

        // TODO aaa-zhengwei 测试用
        // if (msg instanceof DeveloperValidateMessage) {
        // service = developerValidateMessageProcessService;
        // } else {
        // service = messageProcessServiceMap.get(msgType);
        // }
        
        service = messageProcessServiceMap.get(msgType);

        if (service != null) {
            if (log.isDebugEnabled()) {
                log.debug("find MessageProcessService, name=" + service.getClass().getSimpleName());
            }
        } else {

            service = discardMessageProcessService;
        }

        return service;
    }

}
