package com.kongur.monolith.weixin.core.domain.message.normal;

import java.util.Map;

/**
 * 链接消息
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class LinkMessage extends NormalMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 8811416273797547554L;

    /**
     * 链接消息
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public LinkMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 消息链接
     * 
     * @return
     */
    public String getUrl() {
        return this.getString("Url");
    }

    /**
     * 消息标题
     * 
     * @return
     */
    public String getTitle() {
        return this.getString("Title");
    }

    /**
     * 消息描述
     * 
     * @return
     */
    public String getDescription() {
        return this.getString("Description");
    }

}
