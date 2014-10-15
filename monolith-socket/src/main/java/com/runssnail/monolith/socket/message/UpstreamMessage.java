package com.runssnail.monolith.socket.message;

import java.io.Serializable;

import com.runssnail.monolith.socket.message.header.UpstreamHeader;

/**
 * 对方过来的数据对象(包含对方的响应和请求)
 * 
 * @author zhengwei
 */
public interface UpstreamMessage extends Serializable {

    void setUpstreamHeader(UpstreamHeader header);

    /**
     * 报文头
     * 
     * @return
     */
    UpstreamHeader getUpstreamHeader();

    /**
     * 交易码
     * 
     * @return
     */
    String getTransCode();

}
