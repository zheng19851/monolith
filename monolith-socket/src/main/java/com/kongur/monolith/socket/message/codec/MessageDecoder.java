package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * 报文解码器(业务数据部分)
 * 
 * @author zhengwei
 * @param <USO>
 */

public interface MessageDecoder<USO> {

    /**
     * 创建UpstreamMessage
     * 
     * @param header
     * @return
     */
    USO createUpstreamMessage(UpstreamHeader header);

//    /**
//     * 解码
//     * 
//     * @param fixedBuf 定长部分数据
//     * @param multiBuf 循环部分数据
//     * @param header 报文头
//     * @param decoder
//     * @return
//     */
//    DecodeResult<USO> decode(ByteBuffer fixedBuf, ByteBuffer multiBuf, UpstreamHeader header, CharsetDecoder decoder)
//                                                                                                                     throws CodecException;

    /**
     * 解码
     * 
     * @param bodyBuf 报文体部分
     * @param header 报文头
     * @param decoder
     * @return
     */
    DecodeResult<USO> decode(ByteBuffer bodyBuf, UpstreamHeader header, CharsetDecoder decoder) throws CodecException;

}
