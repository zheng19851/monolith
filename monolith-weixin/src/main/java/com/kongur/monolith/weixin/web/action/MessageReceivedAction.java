package com.kongur.monolith.weixin.web.action;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.domain.Message;
import com.kongur.monolith.weixin.domain.ProcessResult;
import com.kongur.monolith.weixin.service.MessageBuilder;
import com.kongur.monolith.weixin.service.MessageProcessService;
import com.kongur.monolith.weixin.service.MessageProcessServiceFactory;

/**
 * 微信消息接受服务
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

        // @RequestBody String receivedMsg

        String receivedMsg = null;

        if (StringUtil.isBlank(echostr)) {
            int len = req.getContentLength();
            try {

                InputStream in = req.getInputStream();
                byte[] dataBytes = new byte[len];
                in.read(dataBytes);

                receivedMsg = new String(dataBytes);
                if (log.isDebugEnabled()) {
                    log.debug("received message->" + receivedMsg + "<-");
                }

            } catch (IOException e) {
                log.error("read receivedMsg error", e);
                return null;
            }

        }

        Message msg = messageBuilder.build(signature, timestamp, nonce, echostr, receivedMsg);
        if (msg == null) {
            return null;
        }

        MessageProcessService messageProcessService = messageProcessServiceFactory.createMessageProcessService(msg.getMsgType());

        if (messageProcessService == null) {
            return null;
        }

        ProcessResult result = messageProcessService.process(msg);

        if (!result.isSuccess()) {
            return null;
        }

        return result.getData();
    }

}
