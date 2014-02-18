package com.kongur.monolith.im.weixin.web.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.domain.message.Message;
import com.kongur.monolith.im.serivce.message.MessageBuilder;
import com.kongur.monolith.im.serivce.message.MessageProcessService;
import com.kongur.monolith.im.serivce.message.MessageProcessServiceFactory;

/**
 * 微信消息接收服务
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Controller
public class MessageReceivedAction {

    private final Logger                 log = Logger.getLogger(getClass());

    @Resource(name = "defaultMessageProcessServiceFactory")
    private MessageProcessServiceFactory messageProcessServiceFactory;

    @Resource(name = "defaultMessageBuilder")
    private MessageBuilder               messageBuilder;

    /**
     * 接收微信推送消息
     * 
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return
     */
    @RequestMapping("weixin/message.htm")
    @ResponseBody
    public String messageReceived(@RequestParam(value = "signature", required = false)
    String signature, @RequestParam(value = "timestamp", required = false)
    String timestamp, @RequestParam(value = "nonce", required = false)
    String nonce, @RequestParam(value = "echostr", required = false)
    String echostr, HttpServletRequest req) {

        Message msg = messageBuilder.build(req);
        if (msg == Message.NULL_MESSAGE) {
            return null;
        }

        MessageProcessService<Message> messageProcessService = messageProcessServiceFactory.createMessageProcessService(msg.getMsgType());

        if (messageProcessService == null) {
            return null;
        }

        ServiceResult<String> result = messageProcessService.process(msg);

        if (!result.isSuccess()) {
            return null;
        }

        return result.getResult();
    }

}
