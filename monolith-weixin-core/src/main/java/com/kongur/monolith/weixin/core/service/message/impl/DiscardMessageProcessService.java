package com.kongur.monolith.weixin.core.service.message.impl;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.domain.message.Message;
import com.kongur.monolith.weixin.core.service.message.AbstractMessageProcessService;

/**
 * 当找不到指定的消息处理服务时，就会调用这个服务
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
// @Service("discardMessageProcessService")
public class DiscardMessageProcessService extends AbstractMessageProcessService<Message> {

    @Override
    public boolean supports(Message msg) {
        return true;
    }

    @Override
    protected void doProcess(Message msg, Result<String> result) {

        log.warn("the message is discarded. msg=" + msg);

    }

}
