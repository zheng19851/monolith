package com.runssnail.monolith.socket.protocol;

import java.nio.ByteBuffer;

import com.runssnail.monolith.socket.message.UpstreamMessage;
import com.runssnail.monolith.socket.message.codec.CodecException;

/**
 * 协议解析器
 * 
 * <p>推荐A + B + C 3段解析
 * A段：整个报文字节长度 (4个字节)
 * B段：报文头(固定长度)
 * C段：报文体(可变)
 * </p>
 * 
 * @author zhengwei
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
