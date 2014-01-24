package com.kongur.monolith.socket.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;

import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.codec.CodecException;

/**
 * @author zhengwei
 */
public abstract class AbstractProtocolParser implements ProtocolParser {

    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    public UpstreamMessage parse(ByteBuffer buffer) throws CodecException {

        UpstreamMessage req = null;

        try {
            req = doParse(buffer);
        } catch (CharacterCodingException e) {
            throw new CodecException(e);
        }

        return req;
    }

    /**
     * 具体的协议子类实现
     * 
     * @param buffer
     * @return
     * @throws CharacterCodingException
     */
    protected abstract UpstreamMessage doParse(ByteBuffer buffer) throws CharacterCodingException;

}
