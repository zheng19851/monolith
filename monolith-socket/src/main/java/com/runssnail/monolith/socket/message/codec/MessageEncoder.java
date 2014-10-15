package com.runssnail.monolith.socket.message.codec;

import java.nio.charset.CharsetEncoder;

import com.runssnail.monolith.socket.message.DownstreamMessage;

/**
 * 报文编码器 (业务数据部分)
 * 
 * @author zhengwei
 * @param <DM>
 */
public interface MessageEncoder<DM extends DownstreamMessage> {

    /**
     * 编码报文体(业务数据部分)
     * 
     * @param dm
     * @param encoder
     * @return
     */
    EncodeResult encode(DM dm, CharsetEncoder encoder) throws CodecException;
}
