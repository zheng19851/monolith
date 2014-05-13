package com.kongur.monolith.weixin.core.service.message.normal;

import com.kongur.monolith.weixin.core.domain.message.EnumMessageType;
import com.kongur.monolith.weixin.core.domain.message.Message;
import com.kongur.monolith.weixin.core.domain.message.normal.TextMessage;
import com.kongur.monolith.weixin.core.service.message.AbstractMessageProcessService;

/**
 * 文本消息处理服务
 * 
 * @author zhengwei
 * @date 2014-2-18
 */
// @Service("textMessageProcessService")
public class TextMessageProcessService extends AbstractMessageProcessService<TextMessage> {

    @Override
    public boolean supports(Message msg) {
        return EnumMessageType.isText(msg.getMsgType());
    }

}
