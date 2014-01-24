package com.kongur.monolith.socket.message.header;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;


/**
 * 响应和发送请求时的报文头
 * 
 * @author zhengwei
 */
public interface DownstreamHeader {

    /**
     * 响应和发送请求时编码报文头
     * 
     * @param header
     * @param encoder
     * @throws CharacterCodingException
     */
    public void encode(ByteBuffer header, CharsetEncoder encoder) throws CharacterCodingException;

    /**
     * 交易代码
     * 
     * @return
     */
    public String getTransCode();

    public void setTransCode(String transCode);

    public boolean isSuccess();

    /**
     * 报文头固定长度(字节)
     * 
     * @return
     */
    public int getBytesLen();

}
