package com.kongur.monolith.weixin.core.service.reply.unactive;

import com.kongur.monolith.weixin.core.domain.Reply;

/**
 * 资源类型
 * 
 * @author zhengwei
 * @date 2014年2月26日
 */
public abstract class UnactiveResourceReplyMessageBuilder<R extends Reply> extends UnactiveVelocityReplyMessageBuilder<R> {

    @Override
    public boolean supports(R reply) {
        return super.supports(reply) && reply.isResource();
    }

}
