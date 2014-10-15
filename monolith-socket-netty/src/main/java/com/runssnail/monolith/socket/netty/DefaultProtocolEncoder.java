package com.runssnail.monolith.socket.netty;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

import com.runssnail.monolith.socket.Constants;
import com.runssnail.monolith.socket.message.DownstreamMessage;
import com.runssnail.monolith.socket.message.UpstreamMessage;
import com.runssnail.monolith.socket.message.codec.CodecException;
import com.runssnail.monolith.socket.message.codec.CodecUtils;
import com.runssnail.monolith.socket.message.codec.EncodeResult;
import com.runssnail.monolith.socket.message.codec.MessageCodecFactory;
import com.runssnail.monolith.socket.message.codec.MessageEncoder;
import com.runssnail.monolith.socket.message.header.DownstreamHeader;


/**
 * 默认的报文协议编码类
 * 
 * @author zhengwei
 * @date 2014-1-26
 */
public class DefaultProtocolEncoder extends SimpleChannelDownstreamHandler {

    protected final Logger                                          log            = Logger.getLogger(getClass());

    private Charset                                                 defaultCharset = Constants.DEFAULT_CHARSET;

    private MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory;

    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
        DownstreamMessage dm = (DownstreamMessage) e.getMessage();

        CharsetEncoder encoder = defaultCharset.newEncoder();

        DownstreamHeader header = dm.getDownstreamHeader(); // 报文头(固定部分)
        ByteBuffer headerBuf = header.encode(encoder);

        if (log.isDebugEnabled()) {
            ByteBuffer headerCopy = headerBuf.duplicate();
            StringBuilder sb = new StringBuilder();
            sb.append("=====================encode header(transCode=" + dm.getTransCode() + ", bytes="
                      + headerCopy.limit() + ")===================");
            sb.append("->" + CodecUtils.getString(headerCopy) + "<-");
            sb.append("=====================encode header===================");
            log.debug(sb.toString());
        }

        MessageEncoder<DownstreamMessage> messageEncoder = messageCodecFactory.getMessageEncoder(dm.getTransCode());
        EncodeResult encodeResult = messageEncoder.encode(dm, encoder);

        if (!encodeResult.isSuccess()) {
            throw new CodecException(dm.getTransCode(), encodeResult.getResultCode(), encodeResult.getResultInfo());
        }

        if (log.isDebugEnabled()) {
            ByteBuffer bodyCopy = encodeResult.getBuffer().duplicate();
            StringBuilder sb = new StringBuilder();
            sb.append("=====================encode body(transCode=" + dm.getTransCode() + ", bytes=" + bodyCopy.limit()
                      + ")===================");
            sb.append("->" + CodecUtils.getString(bodyCopy) + "<-");
            sb.append("=====================encode body===================");
            log.debug(sb.toString());
        }

        int contentLen = headerBuf.limit() + encodeResult.getBuffer().position(); // 内容部分长度

        int totalBytes = getMessageLen() + contentLen;

        ChannelBuffer buf = ChannelBuffers.buffer(getMessageLen() + contentLen);

        writeMessageLen(buf, contentLen);

        buf.writeBytes(headerBuf);
        buf.writeBytes(encodeResult.getBuffer());

        Channels.write(ctx, e.getFuture(), buf);

        if (log.isDebugEnabled()) {
            log.debug("encode " + dm.getTransCode() + " successful! wrote " + totalBytes + " bytes");
        }
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
