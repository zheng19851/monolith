package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;

import org.apache.log4j.Logger;

import com.kongur.monolith.socket.Constants;

/**
 * 数据编码器
 * 
 * @author zhengwei
 * @param <DSO>
 */
public abstract class AbstractMessageEncoder<DSO> implements MessageEncoder<DSO> {

    protected final Logger logger     = Logger.getLogger(getClass());

    private int            bufferSize = Constants.DEFAULT_BUFFER_SIZE;

    @Override
    public EncodeResult encode(DSO dso, CharsetEncoder encoder) throws CodecException {

        if (logger.isDebugEnabled()) {
            logger.debug("=====encode data start=====, dso=" + dso);
        }

        EncodeResult result = new EncodeResult();
        result.setSuccess(true);

        try {
            encodeMixBuf(dso, encoder, result);
        } catch (Exception e) {
            throw new CodecException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("=====encode data end=====, dso=" + dso);
        }

        return result;
    }

    /**
     * 编码
     * 
     * @param dso
     * @param encoder
     * @param result
     * @throws CharacterCodingException
     */
    protected void encodeMixBuf(DSO dso, CharsetEncoder encoder, EncodeResult result) throws CharacterCodingException {

        ByteBuffer fixedBuff = encodeFixedBuf(dso, encoder, result);

        if (!result.isSuccess()) {
            return;
        }

        result.setBuffer(fixedBuff);

        ByteBuffer multiBuff = encodeMultiBuf(dso, encoder);

        result.setMultiBuff(multiBuff);
    }

    /**
     * 编码循环体部分数据
     * 
     * @param dso
     * @param encoder
     * @return
     * @throws CharacterCodingException
     */
    protected abstract ByteBuffer encodeMultiBuf(DSO dso, CharsetEncoder encoder) throws CharacterCodingException;

    /**
     * 编码定长部分数据，除报文头外
     * 
     * @param dso
     * @param fixedBuff
     * @param encoder
     * @param result
     * @throws CharacterCodingException
     */
    protected abstract ByteBuffer encodeFixedBuf(DSO dso, CharsetEncoder encoder, EncodeResult result)
                                                                                                      throws CharacterCodingException;

    /**
     * 分配ByteBuffer
     * 
     * @return
     */
    protected ByteBuffer allocateBuffer() {
        return allocateBuffer(this.bufferSize);
    }

    /**
     * 分配ByteBuffer
     * 
     * @return
     */
    protected ByteBuffer allocateBuffer(int size) {
        return ByteBuffer.allocate(size);
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}
