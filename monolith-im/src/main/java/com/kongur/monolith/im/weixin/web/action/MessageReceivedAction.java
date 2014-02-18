package com.kongur.monolith.im.weixin.web.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kongur.monolith.im.domain.Message;
import com.kongur.monolith.im.domain.ProcessResult;
import com.kongur.monolith.im.serivce.MessageBuilder;
import com.kongur.monolith.im.serivce.MessageProcessService;
import com.kongur.monolith.im.serivce.MessageProcessServiceFactory;

/**
 * ΢����Ϣ���ܷ���
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
     * ����΢��������Ϣ
     * 
     * @param signature ΢�ż���ǩ����signature����˿�������д��token�����������е�timestamp������nonce������
     * @param timestamp ʱ���
     * @param nonce �����
     * @param echostr ����ַ���
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