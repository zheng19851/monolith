package com.kongur.monolith.weixin.core.domain.message.normal;

import java.util.Map;

/**
 * 语音消息
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class VoiceMessage extends NormalMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 3442377982279511620L;

    /**
     * 语音消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public VoiceMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
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

}
