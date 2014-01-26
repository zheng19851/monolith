package com.kongur.monolith.socket.netty;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.message.DownstreamMessage;
import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.codec.EncodeResult;
import com.kongur.monolith.socket.message.codec.MessageCodecFactory;
import com.kongur.monolith.socket.message.codec.MessageEncoder;
import com.kongur.monolith.socket.message.header.DownstreamHeader;

/**
 * 默认的报文协议编码类
 * 
 * @author zhengwei
 * @date 2014-1-26
 */
public class DefaultProtocolEncoder extends SimpleChannelDownstreamHandler {

    private Charset                                                 defaultCharset = Constants.DEFAULT_CHARSET;

    private MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory;

    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
        DownstreamMessage dm = (DownstreamMessage) e.getMessage();

        CharsetEncoder encoder = defaultCharset.newEncoder();

        DownstreamHeader header = dm.getDownstreamHeader(); // 报文头(固定部分)
        ByteBuffer headerBuf = ByteBuffer.allocate(header.getBytesLen());
        header.encode(headerBuf, encoder);
        headerBuf.flip();

        MessageEncoder<DownstreamMessage> messageEncoder = messageCodecFactory.getMessageEncoder(dm.getTransCode());
        EncodeResult encodeResult = messageEncoder.encode(dm, encoder);

        int contentLen = headerBuf.limit() + encodeResult.getBuffer().position(); // 内容部分长度
        ChannelBuffer buf = ChannelBuffers.buffer(getMessageLen() + contentLen);

        writeMessageLen(buf, contentLen);

        buf.writeBytes(headerBuf);
        buf.writeBytes(encodeResult.getBuffer());

        Channels.write(ctx, e.getFuture(), buf);
    }

    protected void writeMessageLen(ChannelBuffer buf, int contentLen) {
        buf.writeInt(contentLen);
    }

    /**
     * 获取
     * 
     * @return
     */
    protected int getMessageLen() {
        return 4;
    }

    public void setMessageCodecFactory(MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory) {
        this.messageCodecFactory = messageCodecFactory;
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

}
