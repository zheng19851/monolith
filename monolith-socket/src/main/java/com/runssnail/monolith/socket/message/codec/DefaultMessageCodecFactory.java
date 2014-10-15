package com.runssnail.monolith.socket.message.codec;

import com.runssnail.monolith.socket.message.DownstreamMessage;
import com.runssnail.monolith.socket.message.UpstreamMessage;

/**
 * 银商系统默认的报文体编解码工厂
 * 
 * @author zhengwei
 */
// @Service("defaultMessageCodecFactory")
public class DefaultMessageCodecFactory<UM extends UpstreamMessage, DM extends DownstreamMessage> extends AbstractMessageCodecFactory<UM, DM> {

}
