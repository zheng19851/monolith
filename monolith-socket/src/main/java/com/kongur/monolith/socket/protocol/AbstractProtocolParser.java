package com.kongur.monolith.socket.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;

import com.kongur.monolith.socket.buffer.ByteBuffers;
import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.codec.CodecException;

/**
 * @author zhengwei
 */
public abstract class AbstractProtocolParser implements ProtocolParser {

    protected final Logger   logger              = Logger.getLogger(getClass());

    /**
     * 默认的A段字节长度
     */
    private static final int DEFAULT_A_BLOCK_LEN = 4;

    /**
     * A段字节长度
     */
    private int              aBlockLen           = DEFAULT_A_BLOCK_LEN;

    /**
     * 整个报文字节长度
     */
    private int              len                 = -1;

    private ByteBuffer       aBlockBuffer        = ByteBuffer.allocate(4);

    @Override
    public UpstreamMessage parse(ByteBuffer buffer) throws CodecException {

        UpstreamMessage req = null;

        try {
            int localLen = readLen(buffer);
            if (localLen != -1) {
                this.len = localLen;
                req = doParse(buffer);
            }
        } catch (CharacterCodingException e) {
            throw new CodecException(e);
        }

        return req;
    }

    /**
     * 解析整个报文长度(默认是头4个字节)
     * 
     * @param buffer
     * @return
     * @throws CharacterCodingException
     */
    protected int readLen(ByteBuffer buffer) throws CharacterCodingException {

        // return buffer.getInt();
        ReadDTO readLen = readBuffer(buffer, new CallBack() {

            @Override
            public void putBuffer(ByteBuffer buffer) {
                aBlockBuffer.put(buffer);
            }

            @Override
            public void newBuffer() {
                // ignore
            }

            @Override
            public boolean hasRead() {
                return len != -1;
            }

            @Override
            public int getFixLen() {
                return aBlockLen;
            }

            @Override
            public int getReadBufferLen() {
                return aBlockBuffer.position();
            }

            @Override
            public String getBlockName() {
                return "A";
            }

            @Override
            public void doSuccess() throws CharacterCodingException {
                aBlockBuffer.flip();
            }
        });

        return aBlockBuffer.getInt();
    }

    /**
     * 解析动态的报文部分，头和体
     * 
     * @param buffer
     * @param beginIndex
     * @param len
     * @return
     * @throws CharacterCodingException
     */
    protected ReadDTO readBuffer(ByteBuffer buffer, CallBack call) throws CharacterCodingException {

        ReadDTO read = new ReadDTO();

        if (call.hasRead()) {
            if (logger.isDebugEnabled()) {
                logger.debug("====" + call.getBlockName() + " block has read===");
            }
            call.newBuffer(); // 当返回出错时，避免bodyBuffer为空
            read.setSuccess(true);
            return read;
        }

        if (!buffer.hasRemaining()) {
            return read;
        }

        call.newBuffer();

        int currPosition = buffer.position();

        int currLen = call.getReadBufferLen();

        int len = call.getFixLen();

        if (currLen == len) {
            read.setSuccess(true);
            return read;
        }

        // 实际读取的字节数
        int readBytes = 0;

        int remain = buffer.limit() - buffer.position(); // buffer 里剩余字节数
        if (currLen == 0) {
            // 说明还没有任何数据
            readBytes = len > remain ? remain : len;

        } else {
            int needBytes = len - currLen; // 还需要的字节数
            readBytes = needBytes > remain ? remain : needBytes;
        }

        ByteBuffer needBuffer = ByteBuffers.getSlice(buffer, currPosition, readBytes); // 返回的是一个新的BUFFER
        call.putBuffer(needBuffer);

        buffer.position(currPosition + readBytes);

        if (call.getReadBufferLen() != len) {
            logger.warn(call.getBlockName() + " buffer has not enough bytes, this block should be " + len
                        + " bytes, but only" + call.getReadBufferLen() + " bytes");
            read.setReadBytes(readBytes);
            return read;
        }

        call.doSuccess();

        read.setReadBytes(readBytes).setSuccess(true);
        return read;
    }

    protected abstract UpstreamMessage doParse(ByteBuffer buffer) throws CharacterCodingException;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

}
