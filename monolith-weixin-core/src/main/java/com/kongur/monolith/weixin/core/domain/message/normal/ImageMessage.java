package com.kongur.monolith.weixin.core.domain.message.normal;

import java.util.Map;

/**
 * 图片消息
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class ImageMessage extends NormalMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 7577533032194810330L;

    /**
     * 图片消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public ImageMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 图片链接
     * 
     * @return
     */
    public String getPicUrl() {
        return this.getString("PicUrl");
    }

    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * 
     * @return
     */
    public String getMediaId() {
        return this.getString("MediaId");
    }

}
