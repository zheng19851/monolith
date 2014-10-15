package com.runssnail.monolith.socket.message.codec;

import com.runssnail.monolith.socket.message.DownstreamMessage;

/**
 * 当报文体为单条数据时使用
 * 
 * @author zhengwei
 * @param <DM>
 */
public abstract class AbstractFixedMessageEncoder<DM extends DownstreamMessage> extends AbstractMessageEncoder<DM> {

}
