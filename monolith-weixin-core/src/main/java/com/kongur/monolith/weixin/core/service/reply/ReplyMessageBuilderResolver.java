package com.kongur.monolith.weixin.core.service.reply;

import com.kongur.monolith.weixin.common.domain.dto.Reply;

/**
 * 查找符合要求的处理服务
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
public interface ReplyMessageBuilderResolver<R extends Reply> {

    /**
     * 查找
     * 
     * @param reply
     * @return
     */
    ReplyMessageBuilder<R> resolve(R reply);

}
