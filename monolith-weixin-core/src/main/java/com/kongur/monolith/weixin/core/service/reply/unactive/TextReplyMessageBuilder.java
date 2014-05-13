package com.kongur.monolith.weixin.core.service.reply.unactive;

import java.util.Map;

import com.kongur.monolith.weixin.core.domain.Reply;
import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * 被动回复文本消息
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
// @Service("textReplyMessageBuilder")
public class TextReplyMessageBuilder extends UnactiveVelocityReplyMessageBuilder<Reply> {

    @Override
    public boolean supports(Reply reply) {
        return super.supports(reply) && reply.isText();
    }

    @Override
    protected void buildModelParams(Reply reply, Message msg, Map model) {

        // ignore
        // model.put("toUser", msg.getFromUserName());
        // model.put("fromUser", msg.getToUserName());
        //
        // model.put("createTime", new Date().getTime());
        // // model.put("msgType", msg.getMsgType());
        //
        // model.put("reply", reply);

    }

}
