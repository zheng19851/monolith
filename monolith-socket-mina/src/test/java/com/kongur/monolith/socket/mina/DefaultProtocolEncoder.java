package com.kongur.monolith.socket.mina;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.message.DownstreamMessage;
import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.codec.CodecException;
import com.kongur.monolith.socket.message.codec.CodecUtils;
import com.kongur.monolith.socket.message.codec.EncodeResult;
import com.kongur.monolith.socket.message.codec.MessageCodecFactory;
import com.kongur.monolith.socket.message.codec.MessageEncoder;

/**
 * 默认的协议的编码器
 * 
 * @author zhengwei
 */
// @Service("defaultProtocolEncoder")
public class DefaultProtocolEncoder extends ProtocolEncoderAdapter {

    protected final Logger                                          log = Logger.getLogger(getClass());

    private MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory;

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput output) throws Exception {

        DownstreamMessage dso = (DownstreamMessage) obj;

        CharsetEncoder encoder = Constants.DEFAULT_CHARSET.newEncoder();

        ByteBuffer header = encodeHeader(dso, encoder);

        MessageEncoder<DownstreamMessage> messageEncoder = messageCodecFactory.getMessageEncoder(dso.getTransCode());

        EncodeResult dataResult = messageEncoder.encode(dso, encoder);

        if (!dataResult.isSuccess()) {
            throw new CodecException(dso.getTransCode(), dataResult.getResultCode(), dataResult.getResultInfo());
        }

        ByteBuffer fixedBuf = dataResult.getBuffer();
        ByteBuffer data = null; // 循环体部分

        int dLen = getDBlockLen(header, fixedBuf);// call.getDBlockLen(header, null, data); // d段字节数
        int eLen = getEBlockLen(data); // e段字节数

        ByteBuffer aBuffer = CodecUtils.getBufferAlignRight(String.valueOf(dLen), DefaultProtocolParser.A_FIX_LEN,
                                                            encoder);
        ByteBuffer bBuffer = CodecUtils.getBufferAlignRight(String.valueOf(eLen), DefaultProtocolParser.B_FIX_LEN,
                                                            encoder);
        ByteBuffer cBuffer = CodecUtils.getBufferAlignLeft(Constants.ZERO, DefaultProtocolParser.C_FIX_LEN, encoder,
                                                           Constants.ZERO_BYTE);

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("\r\n");

            ByteBuffer aBuff = aBuffer.duplicate();
            sb.append("=====a block string ->" + CodecUtils.getString(aBuff, false) + "<-end" + "\r\n");

            ByteBuffer bBuff = bBuffer.duplicate();
            sb.append("=====b block string ->" + CodecUtils.getString(bBuff, false) + "<-end" + "\r\n");

            ByteBuffer cBuff = cBuffer.duplicate();
            sb.append("=====c block string ->" + CodecUtils.getString(cBuff, false) + "<-end" + "\r\n");

            ByteBuffer headerBuff = header.duplicate();
            sb.append("=====header string total bytes(" + headerBuff.limit() + ") ->"
                      + CodecUtils.getString(headerBuff, false) + "<-end" + "\r\n");

            if (fixedBuf != null && fixedBuf.hasRemaining()) {
                ByteBuffer fixBuffCopy = fixedBuf.duplicate();
                sb.append("=====fixed params string total bytes(" + fixBuffCopy.limit() + ") ->"
                                  + CodecUtils.getString(fixBuffCopy, false) + "<-end").append("\r\n");
            } else {
                sb.append("=====fixed params string total bytes(0)").append("\r\n");
            }

            if (data != null && data.hasRemaining()) {
                ByteBuffer bodyBuff = data.duplicate();
                sb.append("=====multi params string total bytes(" + bodyBuff.limit() + ") ->"
                          + CodecUtils.getString(bodyBuff, false) + "<-end");
            } else {
                sb.append("=====multi params string total bytes(0)");
            }

            log.debug(sb.toString());

        }

        StringBuilder sb = new StringBuilder("\r\n");

        ByteBuffer aBuff = aBuffer.duplicate();
        sb.append("=====a block string ->" + CodecUtils.getString(aBuff, false) + "<-end" + "\r\n");

        ByteBuffer bBuff = bBuffer.duplicate();
        sb.append("=====b block string ->" + CodecUtils.getString(bBuff, false) + "<-end" + "\r\n");

        ByteBuffer cBuff = cBuffer.duplicate();
        sb.append("=====c block string ->" + CodecUtils.getString(cBuff, false) + "<-end" + "\r\n");

        ByteBuffer headerBuff = header.duplicate();
        sb.append("=====header string total bytes(" + headerBuff.limit() + ") ->"
                  + CodecUtils.getString(headerBuff, false) + "<-end" + "\r\n");

        if (fixedBuf != null && fixedBuf.hasRemaining()) {
            ByteBuffer fixBuffCopy = fixedBuf.duplicate();
            sb.append("=====fixed params string total bytes(" + fixBuffCopy.limit() + ") ->"
                              + CodecUtils.getString(fixBuffCopy, false) + "<-end").append("\r\n");
        } else {
            sb.append("=====fixed params string total bytes(0)").append("\r\n");
        }

        if (data != null && data.hasRemaining()) {
            ByteBuffer bodyBuff = data.duplicate();
            sb.append("=====multi params string total bytes(" + bodyBuff.limit() + ") ->"
                      + CodecUtils.getString(bodyBuff, false) + "<-end");
        } else {
            sb.append("=====multi params string total bytes(0)");
        }
        System.out.println(sb.toString());

        // 总字节数
        int totalBytes = DefaultProtocolParser.A_FIX_LEN + DefaultProtocolParser.B_FIX_LEN
                         + DefaultProtocolParser.C_FIX_LEN + dLen + eLen;
        ByteBuffer message = ByteBuffer.allocate(totalBytes);

        message.put(aBuffer);
        message.put(bBuffer);
        message.put(cBuffer);
        message.put(header);
        if (fixedBuf != null) {
            message.put(fixedBuf);
        }
        if (data != null) {
            message.put(data);
        }

        message.flip();

        if (log.isDebugEnabled()) {

            StringBuilder build = new StringBuilder("\r\n");

            build.append("===================encode(" + dso.getTransCode() + ")====================" + "\r\n");

            ByteBuffer b = message.duplicate();
            build.append("=====buffer string total bytes(" + message.limit() + ") ->" + CodecUtils.getString(b, false)
                         + "<-end" + "\r\n");

            build.append("===================encode(" + dso.getTransCode() + ")====================" + "\r\n");
            log.debug(build.toString());
        }

        output.write(IoBuffer.wrap(message));
    }

    private int getEBlockLen(ByteBuffer data) {
        return data != null ? data.limit() : 0;
    }

    private int getDBlockLen(ByteBuffer header, ByteBuffer fixedBuff) {
        if (fixedBuff != null) {
            return header.limit() + fixedBuff.limit();
        }

        return header.limit();
    }

    protected ByteBuffer encodeHeader(DownstreamMessage dso, CharsetEncoder encoder) {
        if (log.isDebugEnabled()) {
            log.debug("=====encode Header start...");
        }

        ByteBuffer header = dso.getDownstreamHeader().encode(encoder);

        if (log.isDebugEnabled()) {
            log.debug("=====encode Header end, header bytes=" + header.limit());
        }
        return header;
    }

    public MessageCodecFactory<UpstreamMessage, DownstreamMessage> getMessageCodecFactory() {
        return messageCodecFactory;
    }

    public void setMessageCodecFactory(MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory) {
        this.messageCodecFactory = messageCodecFactory;
    }

}
