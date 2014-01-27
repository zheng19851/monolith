package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * 报文解码器(业务数据部分)
 * 
 * @author zhengwei
 * @param <UM>
 */

public interface MessageDecoder<UM extends UpstreamMessage> {

    /**
     * 创建UpstreamMessage
     * 
     * @param header
     * @return
     */
    UM createUpstreamMessage(UpstreamHeader header);

    /**
     * 解码
     * 
     * @param bodyBuf 报文体部分
     * @param header 报文头
     * @param decoder
     * @return
     */
    DecodeResult<UM> decode(ByteBuffer bodyBuf, UpstreamHeader header, CharsetDecoder decoder) throws CodecException;

}
