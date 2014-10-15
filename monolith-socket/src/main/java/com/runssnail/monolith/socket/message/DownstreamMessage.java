package com.runssnail.monolith.socket.message;

import java.io.Serializable;

import com.runssnail.monolith.socket.message.header.DownstreamHeader;

/**
 * 本方发送的数据对象(包含本方的响应和请求)
 * 
 * @author zhengwei
 */
public interface DownstreamMessage extends Serializable {

    /**
     * 交易代码
     * 
     * @return
     */
    String getTransCode();

    /**
     * 是否成功
     * 
     * @return
     */
    boolean isSuccess();

    /**
     * 获取报文头
     * 
     * @return
     */
    DownstreamHeader getDownstreamHeader();

}
