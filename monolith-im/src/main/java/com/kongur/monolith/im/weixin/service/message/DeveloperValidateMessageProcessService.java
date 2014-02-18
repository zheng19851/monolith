package com.kongur.monolith.im.weixin.service.message;

import org.springframework.stereotype.Service;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.serivce.AbstractMessageProcessService;
import com.kongur.monolith.im.weixin.domain.message.DeveloperValidateMessage;

/**
 * 开发者认证
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("developerValidateMessageProcessService")
public class DeveloperValidateMessageProcessService extends AbstractMessageProcessService<DeveloperValidateMessage> {

    @Override
    protected void doProcess(DeveloperValidateMessage msg, ServiceResult<String> result) {


        result.setResult(msg.getEchostr()); // 验证通过就原样返回随机字符串

    }

}
