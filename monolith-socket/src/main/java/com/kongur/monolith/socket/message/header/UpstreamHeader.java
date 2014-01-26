package com.kongur.monolith.socket.message.header;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

import com.kongur.monolith.socket.message.codec.CodecException;

/**
 * 接收到请求或者收到响应的报文头
 * 
 * @author zhengwei
 */
public interface UpstreamHeader extends Header {

    /**
     * 解码, 接收到请求或者收到响应时，需要实现这个方法
     * 
     * @param header
     * @param decoder
     * @throws CharacterCodingException
     */
    public void decode(ByteBuffer header, CharsetDecoder decoder) throws CodecException;

}
