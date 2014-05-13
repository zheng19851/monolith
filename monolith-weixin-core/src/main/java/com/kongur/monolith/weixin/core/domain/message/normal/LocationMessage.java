package com.kongur.monolith.weixin.core.domain.message.normal;

import java.util.Map;

/**
 * 地理位置消息
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class LocationMessage extends NormalMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 2669328703261957343L;

    /**
     * 地理位置消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public LocationMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 地理位置维度
     * 
     * @return
     */
    public String getLocationX() {
        return this.getString("Location_X");
    }

    /**
     * 地理位置经度
     * 
     * @return
     */
    public String getLocationY() {
        return this.getString("Location_Y");
    }

    /**
     * 地图缩放大小
     * 
     * @return
     */
    public int getScale() {

        String scale = this.getString("Scale");

        return Integer.valueOf(scale);
    }

    /**
     * 地理位置信息
     * 
     * @return
     */
    public String getLabel() {
        return this.getString("Label");
    }

}
