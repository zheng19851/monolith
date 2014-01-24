package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
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
     * ·Ö¸î·û
     */
    protected String       splitChar = Constants.DEFAULT_SPLIT_CHAR;

    /**
     * ¿ÕÐÐ
     */
    protected String       newBreak  = Constants.DEFAULT_NEW_BREAK;

    @Override
    public DecodeResult<USO> decode(ByteBuffer bodyBuf, UpstreamHeader header, CharsetDecoder decoder)
                                                                                                      throws CodecException {

        DecodeResult<USO> result = new DecodeResult<USO>();
        result.setSuccess(true);

        USO uso = createUpstreamMessage(header);
        if (header == null || header.isSuccess()) {
            try {
                doDecode(uso, bodyBuf, decoder, result);
            } catch (Exception e) {
                CodecException ce = new CodecException(e);
                ce.setTransCode(header.getTransCode()).setErrorCode(EnumMessageErrorCode.ERROR.getCode()).setErrorMsg(EnumMessageErrorCode.ERROR.getMsg());
                throw ce;
            }
        }

        result.setUso(uso);

        return result;
    }

    protected abstract void doDecode(USO uso, ByteBuffer bodyBuf, CharsetDecoder decoder, DecodeResult<USO> result)
                                                                                                                   throws CodecException;

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
