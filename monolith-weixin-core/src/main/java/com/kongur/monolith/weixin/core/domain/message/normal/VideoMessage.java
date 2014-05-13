package com.kongur.monolith.weixin.core.domain.message.normal;

import java.util.Map;

/**
 * 视频消息
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class VideoMessage extends NormalMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -7629121241656779367L;

    /**
     * 视频消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public VideoMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * 
     * @return
     */
    public String getMediaId() {
        return this.getString("MediaId");
    }

    /**
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     * 
     * @return
     */
    public String getThumbMediaId() {
        return this.getString("ThumbMediaId");
    }

}
