package com.kongur.monolith.socket.message.header;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

/**
 * 接收到请求或者收到响应的报文头
 * 
 * @author zhengwei
 */
public interface UpstreamHeader {

    /**
     * 解码, 接收到请求或者收到响应时，需要实现这个方法
     * 
     * @param header
     * @param decoder
     * @throws CharacterCodingException
     */
    public void decode(ByteBuffer header, CharsetDecoder decoder) throws CharacterCodingException;

    /**
     * 返回报文头字节长度
     * 
     * @return
     */
    public int getBytesLen();

    /**
     * 是否处理成功(响应成功或者请求成功)，请求理论上总是正确的
     * 
     * @return
     */
    public boolean isSuccess();

    /**
     * 交易代码
     * 
     * @return
     */
    public String getTransCode();

    public void setTransCode(String transCode);

}
