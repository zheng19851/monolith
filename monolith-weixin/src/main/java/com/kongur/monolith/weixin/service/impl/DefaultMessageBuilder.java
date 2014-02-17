package com.kongur.monolith.weixin.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.domain.Message;
import com.kongur.monolith.weixin.domain.message.DefaultMessage;
import com.kongur.monolith.weixin.domain.message.DeveloperValidateMessage;
import com.kongur.monolith.weixin.service.MessageBuilder;
import com.kongur.monolith.weixin.utils.XmlTools;

/**
 * 创建微信消息对象
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("defaultMessageBuilder")
public class DefaultMessageBuilder implements MessageBuilder {

    private final Logger log = Logger.getLogger(getClass());

    @Override
    public Message build(HttpServletRequest req) {

        String signature = req.getParameter("signature"); // 签名
        String timestamp = req.getParameter("timestamp"); // 时间戳
        String nonce = req.getParameter("nonce"); // 随机数
        String echostr = req.getParameter("echostr");// 随机字符串
        String receivedMsg = null; // 接收到的消息

        if (StringUtil.isBlank(echostr)) {
            receivedMsg = readMsg(req);
        }

        Message msg = null;

        if (StringUtil.isNotBlank(echostr)) {
            msg = new DeveloperValidateMessage(signature, timestamp, nonce, echostr);
        } else {
            Map<String, Object> params = null;
            try {
                params = XmlTools.toMap(receivedMsg);
            } catch (DocumentException e) {
                log.error("xml datas convert to Map error", e);
                return null;
            }

            msg = new DefaultMessage(signature, timestamp, nonce, params);
        }

        return msg;

    }

    private String readMsg(HttpServletRequest req) {
        String receivedMsg = null;

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

        return receivedMsg;

    }

}
