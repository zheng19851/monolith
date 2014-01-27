package com.kongur.monolith.socket.message.codec;

import com.kongur.monolith.socket.message.DownstreamMessage;
import com.kongur.monolith.socket.message.UpstreamMessage;

/**
 * 报文体编解码器创建工厂
 * 
 * @author zhengwei
 */
public interface MessageCodecFactory<UM extends UpstreamMessage, DM extends DownstreamMessage> {

    MessageEncoder<DM> getMessageEncoder(String transCode);

    MessageDecoder<UM> getMessageDecoder(String transCode);

}
