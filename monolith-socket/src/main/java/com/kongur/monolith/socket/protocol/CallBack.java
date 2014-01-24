package com.kongur.monolith.socket.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

/**
 * @author zhengwei
 */
public interface CallBack {

    String getBlockName();

    int getFixLen();

    /**
     * 是否已经读完毕
     * 
     * @return
     */
    boolean hasRead();

    /**
     * 已读取的字节长度
     * 
     * @return
     */
    int getReadBufferLen();

    void putBuffer(ByteBuffer buffer);

    void newBuffer();

    void doSuccess() throws CharacterCodingException;

}
