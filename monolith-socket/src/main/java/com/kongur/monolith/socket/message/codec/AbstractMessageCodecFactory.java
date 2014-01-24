package com.kongur.monolith.socket.message.codec;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 报文体编解码器创建工厂基类
 * 
 * @author zhengwei
 * @param <USO>
 * @param <DSO>
 */
public abstract class AbstractMessageCodecFactory<USO, DSO> implements MessageCodecFactory<USO, DSO> {

    protected final Logger                   log             = Logger.getLogger(getClass());

    private Map<String, MessageEncoder<DSO>> messageEncoders = null;

    private Map<String, MessageDecoder<USO>> messageDecoders = null;

    // private MessageEncoder<DSO> errorMessageEncoder = new ErrorMessageEncoder<DSO>();

    public void putMessageEncoder(String transCode, MessageEncoder<DSO> encoder) {

        if (messageEncoders == null) {
            this.messageEncoders = new HashMap<String, MessageEncoder<DSO>>();
        }

        this.messageEncoders.put(transCode, encoder);
    }

    public void putMessageDecoder(String transCode, MessageDecoder<USO> decoder) {
        if (this.messageDecoders == null) {
            this.messageDecoders = new HashMap<String, MessageDecoder<USO>>();
        }

        this.messageDecoders.put(transCode, decoder);
    }

    @Override
    public MessageEncoder<DSO> getMessageEncoder(String transCode) {
        return this.messageEncoders.get(transCode);
    }

    @Override
    public MessageDecoder<USO> getMessageDecoder(String transCode) {
        return this.messageDecoders.get(transCode);
    }

}
