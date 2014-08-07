package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;

/**
 * 点击菜单跳转链接时的事件推送
 * 
 * @author zhengwei
 */
public class ViewEventMessage extends EventMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ViewEventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }


}
