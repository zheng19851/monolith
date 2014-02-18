package com.kongur.monolith.im.serivce.message;

import org.springframework.stereotype.Service;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.domain.message.Message;

/**
 * 当找不到指定的消息处理服务时，就会调用这个服务
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("discardMessageProcessService")
public class DiscardMessageProcessService extends AbstractMessageProcessService<Message> {

    @Override
    protected void doProcess(Message msg, ServiceResult<String> result) {
        // ignore

        log.warn("the message is discarded. msg=" + msg);

    }

}
