package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;



/**
 * 只读取D段定长部分数据
 * 
 * @author zhengwei
 * @param <USO>
 */
public abstract class AbstractFixedMessageDecoder<USO> extends AbstractMessageDecoder<USO> {

    @Override
    protected void doDecodeMultiBuf(USO uso, ByteBuffer multBuffer, CharsetDecoder decoder, DecodeResult<USO> result)
                                                                                                                   throws CharacterCodingException {
        // ignore

    }

}
