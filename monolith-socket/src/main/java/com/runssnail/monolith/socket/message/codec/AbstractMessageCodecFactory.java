package com.runssnail.monolith.socket.message.codec;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.runssnail.monolith.socket.message.DownstreamMessage;
import com.runssnail.monolith.socket.message.UpstreamMessage;

/**
 * 报文体编解码器创建工厂基类
 * 
 * @author zhengwei
 * @param <UM>
 * @param <DM>
 */
public abstract class AbstractMessageCodecFactory<UM extends UpstreamMessage, DM extends DownstreamMessage> implements MessageCodecFactory<UM, DM> {

    protected final Logger                  log             = Logger.getLogger(getClass());

    private Map<String, MessageEncoder<DM>> messageEncoders = null;

    private Map<String, MessageDecoder<UM>> messageDecoders = null;

    // private MessageEncoder<DSO> errorMessageEncoder = new ErrorMessageEncoder<DSO>();

    public void putMessageEncoder(String transCode, MessageEncoder<DM> encoder) {

        if (messageEncoders == null) {
            this.messageEncoders = new HashMap<String, MessageEncoder<DM>>();
        }

        this.messageEncoders.put(transCode, encoder);
    }

    public void putMessageDecoder(String transCode, MessageDecoder<UM> decoder) {
        if (this.messageDecoders == null) {
            this.messageDecoders = new HashMap<String, MessageDecoder<UM>>();
        }

        this.messageDecoders.put(transCode, decoder);
    }

    @Override
    public MessageEncoder<DM> getMessageEncoder(String transCode) {
        return this.messageEncoders.get(transCode);
    }

    @Override
    public MessageDecoder<UM> getMessageDecoder(String transCode) {
        return this.messageDecoders.get(transCode);
    }

}
