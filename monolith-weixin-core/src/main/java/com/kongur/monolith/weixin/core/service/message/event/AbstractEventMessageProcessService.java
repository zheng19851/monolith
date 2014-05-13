package com.kongur.monolith.weixin.core.service.message.event;

import com.kongur.monolith.weixin.core.domain.message.EnumMessageType;
import com.kongur.monolith.weixin.core.domain.message.Message;
import com.kongur.monolith.weixin.core.domain.message.event.EventMessage;
import com.kongur.monolith.weixin.core.service.message.AbstractMessageProcessService;

/**
 * 事件类型处理服务基类
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public abstract class AbstractEventMessageProcessService<M extends EventMessage> extends AbstractMessageProcessService<M> {

    @Override
    public boolean supports(Message msg) {
        return EnumMessageType.isEvent(msg.getMsgType());
    }

}
