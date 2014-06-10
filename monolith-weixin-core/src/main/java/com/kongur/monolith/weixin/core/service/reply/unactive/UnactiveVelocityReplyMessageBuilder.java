package com.kongur.monolith.weixin.core.service.reply.unactive;

import com.kongur.monolith.weixin.common.domain.dto.Reply;
import com.kongur.monolith.weixin.core.service.reply.VelocityReplyMessageBuilder;

/**
 * 被动发送回复
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
public abstract class UnactiveVelocityReplyMessageBuilder<R extends Reply> extends VelocityReplyMessageBuilder<R> {

    @Override
    public boolean supports(R reply) {
        return !reply.isActive();
    }

}
