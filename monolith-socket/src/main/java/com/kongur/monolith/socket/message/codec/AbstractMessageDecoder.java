package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.log4j.Logger;

import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * @author zhengwei
 * @param <USO>
 */
public abstract class AbstractMessageDecoder<USO> implements MessageDecoder<USO> {

    protected final Logger logger    = Logger.getLogger(getClass());

    protected Charset      charset   = Constants.DEFAULT_CHARSET;

    /**
     * 分割符
     */
    protected String       splitChar = Constants.DEFAULT_SPLIT_CHAR;

    /**
     * 空行
     */
    protected String       newBreak  = Constants.DEFAULT_NEW_BREAK;

    @Override
    public DecodeResult<USO> decode(ByteBuffer fixedBuf, ByteBuffer multiBuf, UpstreamHeader header, CharsetDecoder decoder)
                                                                                                                       throws CodecException {
        DecodeResult<USO> result = new DecodeResult<USO>();
        result.setSuccess(true);

        USO uso = createUpstreamMessage(header);

        if (header == null || header.isSuccess()) {
            try {
                doDecodeMixBuf(uso, fixedBuf, multiBuf, decoder, result);
            } catch (Exception e) {
                CodecException ce = new CodecException(e);
                ce.setTransCode(header.getTransCode()).setErrorCode(EnumMessageErrorCode.ERROR.getCode()).setErrorMsg(EnumMessageErrorCode.ERROR.getMsg());
                throw ce;
            }
        }

        result.setUso(uso);

        return result;
    }

    /**
     * 当响应或者请求，D段和E段同时有数据时需要实现这个方法
     * 
     * @param uso
     * @param fixedBuf
     * @param multiBuf
     * @param decoder
     * @param result
     * @throws CharacterCodingException
     */
    protected void doDecodeMixBuf(USO uso, ByteBuffer fixedBuf, ByteBuffer multiBuf, CharsetDecoder decoder,
                                  DecodeResult<USO> result) throws CharacterCodingException {

        doDecodeFixedBuf(uso, fixedBuf, decoder, result);

        if (!result.isSuccess()) {
            return;
        }

        doDecodeMultiBuf(uso, multiBuf, decoder, result);

    }

    /**
     * 解析多条数据部分
     * 
     * @param uso
     * @param multiBuf
     * @param decoder
     * @param result
     * @throws CharacterCodingException
     */
    protected abstract void doDecodeMultiBuf(USO uso, ByteBuffer multiBuf, CharsetDecoder decoder,
                                            DecodeResult<USO> result) throws CharacterCodingException;

    /**
     * 解析定长部分数据
     * 
     * @param uso
     * @param fixedBuf
     * @param decoder
     * @param result
     */
    protected abstract void doDecodeFixedBuf(USO uso, ByteBuffer fixedBuf, CharsetDecoder decoder,
                                             DecodeResult<USO> result) throws CharacterCodingException;

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getSplitChar() {
        return splitChar;
    }

    public void setSplitChar(String splitChar) {
        this.splitChar = splitChar;
    }

    public String getNewBreak() {
        return newBreak;
    }

    public void setNewBreak(String newBreak) {
        this.newBreak = newBreak;
    }

}
