package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;

/**
 * 自定义菜单事件
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class ClickEventMessage extends EventMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -4376011937062217577L;

    /**
     * 自定义菜单事件
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public ClickEventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     * 
     * @return
     */
    public String getEventKey() {
        return this.getString("EventKey");
    }

}
