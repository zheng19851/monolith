package com.kongur.monolith.weixin.core.service.reply.unactive;

import java.util.Map;

import com.kongur.monolith.weixin.core.domain.Reply;
import com.kongur.monolith.weixin.core.domain.enums.EnumReplyType;
import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * 被动回复图片消息类型
 * 
 * @author zhengwei
 * @date 2014年2月26日
 */
public class ImageResourceReplyMessageBuilder extends UnactiveResourceReplyMessageBuilder<Reply> {

    @Override
    public boolean supports(Reply reply) {
        return super.supports(reply) && EnumReplyType.isImage(reply.getType());
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
