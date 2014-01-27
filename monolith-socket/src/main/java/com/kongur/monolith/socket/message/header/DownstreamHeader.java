package com.kongur.monolith.socket.message.header;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;

import com.kongur.monolith.socket.message.codec.CodecException;

/**
 * 响应和发送请求时的报文头
 * 
 * @author zhengwei
 */
public interface DownstreamHeader extends Header {

    /**
     * 响应和发送请求时编码报文头
     * 
     * @param header
     * @param encoder
     * @throws CharacterCodingException
     */
    public ByteBuffer encode(CharsetEncoder encoder) throws CodecException;

}
