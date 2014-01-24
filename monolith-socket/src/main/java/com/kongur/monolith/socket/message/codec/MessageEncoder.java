package com.kongur.monolith.socket.message.codec;

import java.nio.charset.CharsetEncoder;


/**
 * 报文编码器 (业务数据部分)
 * 
 * @author zhengwei
 *
 * @param <DSO>
 */
public interface MessageEncoder<DSO> {

    /**
     * 编码报文体(业务数据部分)
     * 
     * @param dso
     * @param encoder
     * @return
     */
    EncodeResult encode(DSO dso, CharsetEncoder encoder) throws CodecException;
}
