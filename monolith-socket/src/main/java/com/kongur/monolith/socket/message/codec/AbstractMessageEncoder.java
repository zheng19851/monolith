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
            ByteBuffer bodyBuf = doEncode(dso, encoder);

            result.setBuffer(bodyBuf);
        } catch (Exception e) {
            throw new CodecException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("=====encode data end=====, dso=" + dso);
        }

        return result;
    }

    protected abstract ByteBuffer doEncode(DSO dso, CharsetEncoder encoder) throws CharacterCodingException;

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
