package com.kongur.monolith.socket.message.codec;

/**
 * 报文体编解码器创建工厂
 * 
 * @author zhengwei
 */
public interface MessageCodecFactory<USO, DSO> {

    MessageEncoder<DSO> getMessageEncoder(String transCode);

    MessageDecoder<USO> getMessageDecoder(String transCode);

}
