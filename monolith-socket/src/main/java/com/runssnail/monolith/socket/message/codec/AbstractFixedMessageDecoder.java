package com.runssnail.monolith.socket.message.codec;

import com.runssnail.monolith.socket.message.UpstreamMessage;

/**
 * 只读取D段定长部分数据
 * 
 * @author zhengwei
 * @param <UM>
 */
public abstract class AbstractFixedMessageDecoder<UM extends UpstreamMessage> extends AbstractMessageDecoder<UM> {

}
