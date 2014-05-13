package com.kongur.monolith.weixin.core.domain.message.event;

import java.util.Map;

/**
 * 扫描带参数二维码事件
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class ScanQRCodeEventMessage extends EventMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -3125036471584087414L;

    /**
     * 扫描带参数二维码事件
     * 
     * @param signature
     * @param timestamp
     * @param nonce
     * @param params
     */
    public ScanQRCodeEventMessage(String signature, String timestamp, String nonce, Map<String, Object> params) {
        super(signature, timestamp, nonce, params);
    }

    /**
     * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
     * 
     * @return
     */
    public String getEventKey() {
        return this.getString("EventKey");
    }

    /**
     * 二维码的ticket，可用来换取二维码图片
     * 
     * @return
     */
    public String getTicket() {
        return this.getString("Ticket");
    }

}
