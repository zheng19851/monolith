package com.kongur.monolith.weixin.domain;

import java.util.Map;

import com.kongur.monolith.common.DomainBase;

/**
 * @author zhengwei
 * @date 2014-2-14
 */
public abstract class AbstractMessage extends DomainBase implements Message {

    /**
     * 
     */
    private static final long   serialVersionUID = -2293661716502114332L;

    /**
     * 微信加密签名
     */
    private String              signature;

    /**
     * 时间戳
     */
    private String              timestamp;

    /**
     * 随机数
     */
    private String              nonce;

    /**
     * 消息类型
     */
    private String              msgType;

    /**
     * 业务数据
     */
    private Map<String, Object> params           = null;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public AbstractMessage(String signature, String timestamp, String nonce) {
        this(signature, timestamp, nonce, null);
    }

    public AbstractMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.params = params;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Map<String, Object> getParams() {
        return this.params;
    }

    @Override
    public String getParamString(String key) {
        Object obj = getParam(key);

        return obj != null ? obj.toString() : null;
    }

    @Override
    public Object getParam(String key) {
        return this.params.get(key);
    }

}
