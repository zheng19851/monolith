package com.kongur.monolith.im.weixin.service.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.kongur.monolith.im.domain.message.Message;
import com.kongur.monolith.im.serivce.message.MessageBuilder;
import com.kongur.monolith.im.utils.XmlTools;
import com.kongur.monolith.im.weixin.domain.message.DefaultMessage;
import com.kongur.monolith.im.weixin.domain.message.DeveloperValidateMessage;
import com.kongur.monolith.im.weixin.domain.message.normal.TextMessage;
import com.kongur.monolith.lang.StringUtil;

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

        Message msg = Message.NULL_MESSAGE;

        if (StringUtil.isNotBlank(echostr)) {
            msg = new DeveloperValidateMessage(signature, timestamp, nonce, echostr);
        } else {
            String receivedMsg = readMsg(req);// 接收到的消息
            if (StringUtil.isBlank(receivedMsg)) {
                log.error("can not read receivedMsg from request.");
                return msg;
            }

            Map<String, Object> params = null;
            try {
                params = XmlTools.toMap(receivedMsg);
            } catch (DocumentException e) {
                log.error("xml datas convert to Map error", e);
                return msg;
            }

            String msgType = (String) params.get("MsgType");

            if ("text".equalsIgnoreCase(msgType)) {
                msg = new TextMessage(signature, timestamp, nonce, params);
            } else {
                msg = new DefaultMessage(signature, timestamp, nonce, params);
            }

        }

        if (msg != null) {
            if (log.isDebugEnabled()) {
                log.debug("build message successfully, message=" + msg);
            }
        }

        return msg;

    }

    private String readMsg(HttpServletRequest req) {
        String receivedMsg = null;

        int len = req.getContentLength();

        if (len <= 0) {
            return null;
        }

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
