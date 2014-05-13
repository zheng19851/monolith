package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;

/**
 * 上报地理位置事件
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class LocationEventMessage extends EventMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 9175040775006415998L;

    /**
     * 上报地理位置事件
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public LocationEventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 地理位置纬度
     * 
     * @return
     */
    public String getLatitude() {

        return this.getString("Latitude");
    }

    /**
     * 地理位置经度
     * 
     * @return
     */
    public String getLongitude() {
        return this.getString("Longitude");
    }

    /**
     * 地理位置精度
     * 
     * @return
     */
    public String getPrecision() {
        return this.getString("Precision");
    }
}
