package com.kongur.monolith.socket.protocol;

import java.nio.ByteBuffer;

import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.codec.CodecException;

/**
 * 协议解析器
 * 
 * @author zhengwei
 *
 */
public interface ProtocolParser {

    /**
     * 解析
     * 
     * @param buffer
     * @return
     */
    UpstreamMessage parse(ByteBuffer buffer) throws CodecException;
    
}
