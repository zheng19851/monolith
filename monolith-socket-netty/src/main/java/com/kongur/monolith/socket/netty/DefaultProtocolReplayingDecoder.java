package com.kongur.monolith.socket.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import com.kongur.monolith.socket.netty.DefaultProtocolReplayingDecoder.DecoderState;
import com.kongur.monolith.socket.protocol.ProtocolParser;

/**
 * 默认的协议解析类
 * 
 * @author zhengwei
 * @date 2014-1-26
 */
public class DefaultProtocolReplayingDecoder extends ReplayingDecoder<DecoderState> {

    /**
     * 报文体内容长度
     */
    private int            length         = -1;

    private ProtocolParser protocolParser = null;

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buf, DecoderState state)
                                                                                                               throws Exception {

        switch (state) {
            case READ_LENGTH:
                this.length = buf.readInt(); // 读取报文长度
                checkpoint(DecoderState.READ_CONTENT);
            case READ_CONTENT:
                ChannelBuffer frame = buf.readBytes(this.length);
                checkpoint(DecoderState.READ_LENGTH);
                return protocolParser.parse(frame.toByteBuffer()); // 报文完整后才解析报文
            default:
                throw new Error("Shouldn't reach here.");
        }

    }

    public ProtocolParser getProtocolParser() {
        return protocolParser;
    }

    public void setProtocolParser(ProtocolParser protocolParser) {
        this.protocolParser = protocolParser;
    }

    enum DecoderState {

        READ_LENGTH, READ_CONTENT

    }

}
