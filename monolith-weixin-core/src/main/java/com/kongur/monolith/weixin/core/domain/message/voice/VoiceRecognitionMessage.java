package com.kongur.monolith.weixin.core.domain.message.voice;

import java.util.Map;

import com.kongur.monolith.weixin.core.domain.message.AbstractMessage;

/**
 * 语言识别结果消息
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public class VoiceRecognitionMessage extends AbstractMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -4930721385706842976L;

    /**
     * 语言识别结果消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public VoiceRecognitionMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    // /**
    // * 消息id，64位整型
    // *
    // * @return
    // */
    // public String getMsgId() {
    // return this.getString("MsgId");
    // }

    /**
     * 语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体
     * 
     * @return
     */
    public String getMediaId() {
        return this.getString("MediaId");
    }

    /**
     * 语音格式，如amr，speex等
     * 
     * @return
     */
    public String getFormat() {
        return this.getString("Format");
    }

    /**
     * 语音识别结果，UTF8编码
     * 
     * @return
     */
    public String getRecognition() {
        return this.getString("Recognition");
    }

}
