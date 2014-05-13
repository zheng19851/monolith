package com.kongur.monolith.weixin.core.service.message.impl;

import org.springframework.stereotype.Service;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.domain.message.DeveloperValidateMessage;
import com.kongur.monolith.weixin.core.domain.message.EnumMessageType;
import com.kongur.monolith.weixin.core.domain.message.Message;
import com.kongur.monolith.weixin.core.service.message.AbstractMessageProcessService;

/**
 * 开发者认证
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("developerValidateMessageProcessService")
public class DeveloperValidateMessageProcessService extends AbstractMessageProcessService<DeveloperValidateMessage> {

    @Override
    protected void doProcess(DeveloperValidateMessage msg, Result<String> result) {

        result.setResult(msg.getEchostr()); // 验证通过就原样返回随机字符串

    }

    @Override
    public boolean supports(Message msg) {
        return EnumMessageType.isDeveloperValidate(msg.getMsgType());
    }

}
