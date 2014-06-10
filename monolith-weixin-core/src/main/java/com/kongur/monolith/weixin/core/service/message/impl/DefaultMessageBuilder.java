package com.kongur.monolith.weixin.core.service.message.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.core.domain.message.DefaultMessage;
import com.kongur.monolith.weixin.core.domain.message.DeveloperValidateMessage;
import com.kongur.monolith.weixin.core.domain.message.EnumEventType;
import com.kongur.monolith.weixin.core.domain.message.EnumMessageType;
import com.kongur.monolith.weixin.core.domain.message.Message;
import com.kongur.monolith.weixin.core.domain.message.event.ClickEventMessage;
import com.kongur.monolith.weixin.core.domain.message.event.LocationEventMessage;
import com.kongur.monolith.weixin.core.domain.message.event.ScanQRCodeEventMessage;
import com.kongur.monolith.weixin.core.domain.message.event.SubscribeEventMessage;
import com.kongur.monolith.weixin.core.domain.message.event.UnsubscribeEventMessage;
import com.kongur.monolith.weixin.core.domain.message.normal.ImageMessage;
import com.kongur.monolith.weixin.core.domain.message.normal.LinkMessage;
import com.kongur.monolith.weixin.core.domain.message.normal.LocationMessage;
import com.kongur.monolith.weixin.core.domain.message.normal.TextMessage;
import com.kongur.monolith.weixin.core.domain.message.normal.VideoMessage;
import com.kongur.monolith.weixin.core.domain.message.normal.VoiceMessage;
import com.kongur.monolith.weixin.core.domain.message.voice.VoiceRecognitionMessage;
import com.kongur.monolith.weixin.core.service.message.MessageBuilder;
import com.kongur.monolith.weixin.core.utils.XmlTools;

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
        return this.build(signature, timestamp, nonce, echostr, req);
    }

    private String readMsg(HttpServletRequest req) {
        String receivedMsg = null;

        int len = req.getContentLength();

        if (len <= 0) {
            log.error("can not find any content in the request. len=" + len);
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
            log.error("read receivedMsg error, req=" + req, e);
            return null;
        }

        return receivedMsg;

    }

    @Override
    public Message build(String signature, String timestamp, String nonce, String echostr, HttpServletRequest req) {
        Message msg = Message.NULL_MESSAGE;

        if (StringUtil.isNotBlank(echostr)) {
            msg = new DeveloperValidateMessage(signature, timestamp, nonce, echostr);
        } else {
            String receivedMsg = readMsg(req);// 接收到的消息
            if (StringUtil.isBlank(receivedMsg)) {
                log.error("can not read receivedMsg from the request. req=" + req);
                return msg;
            }

            Map<String, Object> params = null;
            try {
                params = XmlTools.toMap(receivedMsg);
            } catch (DocumentException e) {
                log.error("xml datas convert to Map error, receivedMsg=" + receivedMsg, e);
                return msg;
            }

            // 消息类型
            String msgType = (String) params.get("MsgType");

            if (EnumMessageType.isText(msgType)) {
                msg = new TextMessage(signature, timestamp, nonce, params);
            } else if (EnumMessageType.isImage(msgType)) {
                msg = new ImageMessage(signature, timestamp, nonce, params);
            } else if (EnumMessageType.isVoice(msgType)) {
                // normal voice or Voice Recognition
                String recognition = (String) params.get("Recognition"); // 语音识别结果
                if (recognition != null) {
                    msg = new VoiceRecognitionMessage(signature, timestamp, nonce, params);
                } else {
                    msg = new VoiceMessage(signature, timestamp, nonce, params);
                }

            } else if (EnumMessageType.isVideo(msgType)) {
                msg = new VideoMessage(signature, timestamp, nonce, params);
            } else if (EnumMessageType.isLocation(msgType)) {
                msg = new LocationMessage(signature, timestamp, nonce, params);
            } else if (EnumMessageType.isLink(msgType)) {
                msg = new LinkMessage(signature, timestamp, nonce, params);
            } else if (EnumMessageType.isEvent(msgType)) {

                // 事件类型
                String eventType = (String) params.get("Event");
                if (EnumEventType.isSubscribe(eventType)) {

                    String ticket = (String) params.get("Ticket"); // 二维码的ticket，可用来换取二维码图片
                    if (ticket != null) {
                        msg = new ScanQRCodeEventMessage(signature, timestamp, nonce, params);
                    } else {
                        msg = new SubscribeEventMessage(signature, timestamp, nonce, params);
                    }

                } else if (EnumEventType.isUnSubscribe(eventType)) {
                    msg = new UnsubscribeEventMessage(signature, timestamp, nonce, params);
                } else if (EnumEventType.isLocation(eventType)) {
                    msg = new LocationEventMessage(signature, timestamp, nonce, params);
                } else if (EnumEventType.isClick(eventType)) {
                    msg = new ClickEventMessage(signature, timestamp, nonce, params);
                }

            } else {
                msg = new DefaultMessage(signature, timestamp, nonce, params);
            }

        }

        if (msg.isValid()) {
            if (log.isDebugEnabled()) {
                log.debug("build message successfully, message=" + msg);
            }
        }

        return msg;
    }

}
