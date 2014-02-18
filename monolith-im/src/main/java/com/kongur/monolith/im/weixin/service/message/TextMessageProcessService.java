package com.kongur.monolith.im.weixin.service.message;

import java.text.MessageFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.serivce.AbstractMessageProcessService;
import com.kongur.monolith.im.weixin.domain.message.normal.TextMessage;

/**
 * 文本消息处理服务
 * 
 * @author zhengwei
 * @date 2014-2-18
 */
@Service("textMessageProcessService")
public class TextMessageProcessService extends AbstractMessageProcessService<TextMessage> {

    @Override
    protected void doProcess(TextMessage msg, ServiceResult<String> result) {
        
        // <xml>
        // <ToUserName><![CDATA[toUser]]></ToUserName>
        // <FromUserName><![CDATA[fromUser]]></FromUserName>
        // <CreateTime>12345678</CreateTime>
        // <MsgType><![CDATA[text]]></MsgType>
        // <Content><![CDATA[你好]]></Content>
        // </xml>

        String pattern = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2}</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[{3}]]></Content></xml>";

        String data = MessageFormat.format(pattern, msg.getFromUserName(), msg.getToUserName(), new Date().getTime(),
                                           "it is test!");

        result.setResult(data);
    }

}
