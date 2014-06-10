package com.kongur.monolith.weixin.core.service.reply.active;

import com.kongur.monolith.weixin.common.domain.dto.Reply;
import com.kongur.monolith.weixin.core.service.reply.VelocityReplyMessageBuilder;

/**
 * 主动发送回复
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
public abstract class ActiveVelocityReplyMessageBuilder<R extends Reply> extends VelocityReplyMessageBuilder<R> {

    @Override
    public boolean supports(R reply) {
        return reply.isActive();
    }

}
