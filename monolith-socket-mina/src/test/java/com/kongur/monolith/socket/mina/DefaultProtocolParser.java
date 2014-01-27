package com.kongur.monolith.socket.mina;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.buffer.ByteBuffers;
import com.kongur.monolith.socket.message.DownstreamMessage;
import com.kongur.monolith.socket.message.UpstreamMessage;
import com.kongur.monolith.socket.message.codec.CodecException;
import com.kongur.monolith.socket.message.codec.CodecUtils;
import com.kongur.monolith.socket.message.codec.DecodeResult;
import com.kongur.monolith.socket.message.codec.EnumMessageErrorCode;
import com.kongur.monolith.socket.message.codec.MessageCodecFactory;
import com.kongur.monolith.socket.message.codec.MessageDecoder;
import com.kongur.monolith.socket.message.header.UpstreamHeader;
import com.kongur.monolith.socket.protocol.AbstractProtocolParser;

/**
 * 交易前置协议解析器
 * 
 * @author zhengwei
 */
public class DefaultProtocolParser extends AbstractProtocolParser {

    /**
     * 整个报文长度，固定8个字节
     */
    public static final int                                         A_FIX_LEN     = 4;

    /**
     * B段表示报文头长度(固定长度8个字节)
     */
    public static final int                                         B_FIX_LEN     = 8;

    public static final int                                         C_FIX_LEN     = 16;

    /**
     * a段内容
     */
    private ByteBuffer                                              aBuffer       = ByteBuffer.allocate(A_FIX_LEN); // 默认8个字节

    /**
     * b段
     */
    private ByteBuffer                                              bBuffer       = ByteBuffer.allocate(B_FIX_LEN); // 默认8个字节

    /**
     * c段
     */
    private ByteBuffer                                              cBuffer       = ByteBuffer.allocate(C_FIX_LEN); // 默认16个字节

    /**
     * a段
     */
    private String                                                  aBlock        = "";

    /**
     * b段
     */
    private String                                                  bBlock        = "";

    /**
     * c段
     */
    private String                                                  cBlock        = "";

    /**
     * 从a段解析出来的d段长度
     */
    private int                                                     dBytesLen     = 60; // -1 

    /**
     * 从b段解析出来的e段长度
     */
    private int                                                     eBytesLen     = -1;

    private ByteBuffer                                              dBuffer       = null;

    /**
     * d段是否读完
     */
    private boolean                                                 hasReadDBlock = false;

    private ByteBuffer                                              eBuffer       = null;

    /**
     * E段是否读完
     */
    private boolean                                                 hasReadEBlock = false;

    /**
     * 头部信息
     */
    private ByteBuffer                                              headerBuffer;

    /**
     * D段里定长部分数据
     */
    private ByteBuffer                                              fixedBuffer;

    /**
     * 多条记录部分数据
     */
    private ByteBuffer                                              multBuffer;

    private MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory;
    
    private int  len = -1;

    public DefaultProtocolParser(MessageCodecFactory<UpstreamMessage, DownstreamMessage> messageCodecFactory) {
        this.messageCodecFactory = messageCodecFactory;
    }

    @Override
    protected UpstreamMessage doParse(ByteBuffer buffer) throws CharacterCodingException {

        final CharsetDecoder decoder = Constants.DEFAULT_CHARSET.newDecoder();

        if (logger.isDebugEnabled()) {
            logger.debug("doParse->buffer size:" + (buffer.limit() - buffer.position()));
        }

        // a段
        ReadDTO readA = readBuffer(buffer, new CallBack() {

            @Override
            public void putBuffer(ByteBuffer buffer) {
                aBuffer.put(buffer);
            }

            @Override
            public int getFixLen() {
                return A_FIX_LEN;
            }

            @Override
            public int getReadBufferLen() {
                return aBuffer.position();
            }

            @Override
            public void newBuffer() {

            }

            @Override
            public void doSuccess() throws CharacterCodingException {
                aBuffer.flip();

//                aBlock = CodecUtils.getString(aBuffer, false);// aBuffer.getString(decoder);

//                if (StringUtil.isBlank(aBlock) || aBlock.length() != A_FIX_LEN) {
//                    logger.error("a block error, aBlock=" + aBlock);
//                    throw new CodecException("error");
//                }
//
//                dBytesLen = Integer.valueOf(aBlock);
                
                len = aBuffer.getInt();

                if (logger.isDebugEnabled()) {
                    logger.debug("====read a block success====, ->" + len + "<-");
                }
            }

            @Override
            public boolean hasRead() {
                return len != -1;
            }

            @Override
            public String getBlockName() {
                return "a";
            }
        });

        if (!readA.success) {
            return null;
        }

        // b 段
//        ReadDTO readB = readBuffer(buffer, new CallBack() {
//
//            @Override
//            public void putBuffer(ByteBuffer buffer) {
//                bBuffer.put(buffer);
//            }
//
//            @Override
//            public int getFixLen() {
//                return B_FIX_LEN;
//            }
//
//            @Override
//            public int getReadBufferLen() {
//                return bBuffer.position();
//            }
//
//            @Override
//            public void newBuffer() {
//
//            }
//
//            @Override
//            public void doSuccess() throws CharacterCodingException {
//
//                bBuffer.flip();
//
//                bBlock = CodecUtils.getString(bBuffer, false);// bBuffer.getString(decoder);
//
//                if (StringUtil.isBlank(bBlock) || bBlock.length() != B_FIX_LEN) {
//                    logger.error("b block error, bBlock=" + bBlock);
//                    throw new CodecException(EnumMessageErrorCode.MESSAGE_ERROR);
//                }
//
//                eBytesLen = Integer.valueOf(bBlock);
//
//                if (logger.isDebugEnabled()) {
//                    logger.debug("====read b block success====, ->" + bBlock + "<-");
//                }
//            }
//
//            @Override
//            public boolean hasRead() {
//                return StringUtil.isNotBlank(bBlock);
//            }
//
//            @Override
//            public String getBlockName() {
//                return "b";
//            }
//        });
//
//        if (!readB.success) {
//            return null;
//        }
//
//        // c 段
//        ReadDTO readC = readBuffer(buffer, new CallBack() {
//
//            @Override
//            public void putBuffer(ByteBuffer buffer) {
//                cBuffer.put(buffer);
//            }
//
//            @Override
//            public int getFixLen() {
//                return C_FIX_LEN;
//            }
//
//            @Override
//            public int getReadBufferLen() {
//                return cBuffer.position();
//            }
//
//            @Override
//            public void newBuffer() {
//            }
//
//            @Override
//            public void doSuccess() throws CharacterCodingException {
//                cBuffer.flip();
//
//                cBlock = CodecUtils.getString(cBuffer, false);// cBuffer.getString(decoder);
//
//                if (StringUtil.isBlank(cBlock) || cBlock.length() != getFixLen()) {
//                    logger.error("c block error, cBlock=" + cBlock);
//                    throw new CodecException(EnumMessageErrorCode.MESSAGE_ERROR);
//                }
//
//                if (logger.isDebugEnabled()) {
//                    logger.debug("====read c block success====, ->" + cBlock + "<-");
//                }
//
//            }
//
//            @Override
//            public boolean hasRead() {
//                return StringUtil.isNotBlank(cBlock);
//            }
//
//            @Override
//            public String getBlockName() {
//                return "c";
//            }
//        });
//
//        if (!readC.success) {
//            return null;
//        }

        // d 段 header
        ReadDTO readheader = readBuffer(buffer, new CallBack() {

            @Override
            public void putBuffer(ByteBuffer buffer) {
                dBuffer.put(buffer);
            }

            @Override
            public void newBuffer() {
                if (dBuffer == null) {
                    dBuffer = ByteBuffer.allocate(dBytesLen); // dBytesLen
                }
            }

            @Override
            public int getFixLen() {
                return dBytesLen;
            }

            @Override
            public int getReadBufferLen() {
                return dBuffer.position();
            }

            @Override
            public void doSuccess() throws CharacterCodingException {

                if (getReadBufferLen() != getFixLen()) {
                    logger.error("read d block error");
                    throw new CodecException(EnumMessageErrorCode.MESSAGE_ERROR);
                }

                hasReadDBlock = true;

                dBuffer.flip();

                if (logger.isDebugEnabled()) {
                    ByteBuffer d = dBuffer.duplicate();
                    logger.debug("====read d block success====, ->" + CodecUtils.getString(d, false) + "<-");
                }
                
                eBytesLen = len - 4 - dBytesLen;
            }

            @Override
            public boolean hasRead() {
                return hasReadDBlock;
            }

            @Override
            public String getBlockName() {
                return "d";
            }
        });

        if (!readheader.success) {
            return null;
        }

        // e段 body
        ReadDTO readBody = readBuffer(buffer, new CallBack() {

            @Override
            public void putBuffer(ByteBuffer buffer) {
                eBuffer.put(buffer);
            }

            @Override
            public void newBuffer() {
                if (eBuffer == null) {
                    eBuffer = ByteBuffer.allocate(eBytesLen); // 整个报文 - A- B
                }
            }

            @Override
            public int getFixLen() {
                return eBytesLen;
            }

            @Override
            public int getReadBufferLen() {
                return eBuffer.position();
            }

            @Override
            public void doSuccess() throws CharacterCodingException {
                if (getReadBufferLen() != getFixLen()) {
                    logger.error("read e block error");
                    throw new CodecException(EnumMessageErrorCode.MESSAGE_ERROR);
                }

                hasReadEBlock = true;

                eBuffer.flip();

                if (logger.isDebugEnabled()) {
                    ByteBuffer e = eBuffer.duplicate();
                    logger.debug("====read e block success====, ->" + CodecUtils.getString(e, false) + "<-");
                }
            }

            @Override
            public boolean hasRead() {
                if (eBytesLen == 0) { // 返回的报文出错时，报文体是没有内容的
                    return true;
                }
                return hasReadEBlock;
            }

            @Override
            public String getBlockName() {
                return "e";
            }
        });

        if (!readBody.success) {
            return null;
        }

        // 开始解析请求头和报文体了.....

        UpstreamHeader header = createUpstreamHeader();

        // 报文头
        this.headerBuffer = ByteBuffers.getSlice(this.dBuffer, 0, header.getBytesLen());
        this.dBuffer.position(header.getBytesLen()); // 设置position, 如果有剩余，那就是数据来滴

        if (logger.isDebugEnabled()) {
            ByteBuffer h = this.headerBuffer.duplicate();
            logger.debug("====read header string ->" + CodecUtils.getString(h, false) + "<-");
        }

        this.fixedBuffer = getFixedBuffer();

        this.multBuffer = this.eBuffer;

        header.decode(this.headerBuffer, decoder);

        if (logger.isDebugEnabled()) {
            logger.debug("=====decode header success, header=" + header);
        }

        // 系统错误或者没有交易代码
        // if (header.isSysError() || StringUtil.isBlank(header.getTransCode())) {
        //
        // logger.error("=========================parse UpstreamMessage system error, header=" + header);
        //
        // ErrorUpstreamMessage eso = new ErrorUpstreamMessage(header);
        // if (logger.isDebugEnabled()) {
        // log(header, eso);
        // }
        // return eso;
        // }

        MessageDecoder<UpstreamMessage> messageDecoder = messageCodecFactory.getMessageDecoder(header.getTransCode());
        DecodeResult<UpstreamMessage> result = messageDecoder.decode(this.multBuffer, header, decoder);

        if (!result.isSuccess()) {
            throw new CodecException(header.getTransCode(), result.getResultCode(), result.getResultInfo());
        }

        // 成功了
        UpstreamMessage um = result.getUm();

        if (logger.isDebugEnabled()) {
            log(header, um);
        }

        return um;
    }

    /**
     * 定长部分请求参数
     * 
     * @return
     * @throws CharacterCodingException
     */
    private ByteBuffer getFixedBuffer() throws CharacterCodingException {

        ByteBuffer fixedBuf = null;
        if (this.dBuffer.hasRemaining()) {
            fixedBuf = ByteBuffers.getSlice(this.dBuffer, this.dBuffer.position(),
                                            this.dBuffer.limit() - this.dBuffer.position());
        }

        if (logger.isDebugEnabled()) {
            if (fixedBuf != null) {
                ByteBuffer data = fixedBuf.duplicate();
                logger.debug("====fixed params string ->" + CodecUtils.getString(data, false) + "<-end");
            } else {
                logger.debug("====there is no params in fixed buffer");
            }

        }

        return fixedBuf;
    }

    /**
     * 打印日志
     * 
     * @param header
     * @param uso
     */
    private void log(UpstreamHeader header, UpstreamMessage uso) {
        StringBuilder sb = new StringBuilder("\r\n");

        sb.append("=============================decode(" + uso.getTransCode() + ")============================\r\n");

        String logLenStr = this.toString();
        sb.append(logLenStr).append("\r\n");

        sb.append("header=" + header).append("\r\n");

        sb.append("UpstreamMessage=" + uso + "\r\n");

        sb.append("=============================decode(" + uso.getTransCode() + ")============================");

        logger.debug(sb.toString());
    }

    /**
     * 解析动态的报文部分，头和体
     * 
     * @param buffer
     * @param beginIndex
     * @param len
     * @return
     * @throws CharacterCodingException
     */
    private ReadDTO readBuffer(ByteBuffer buffer, CallBack call) throws CharacterCodingException {

        ReadDTO read = new ReadDTO();
        if (call.hasRead()) {
            if (logger.isDebugEnabled()) {
                logger.debug("====" + call.getBlockName() + " block has read===");
            }
            call.newBuffer(); // 当返回出错时，避免bodyBuffer为空
            read.success = true;
            return read;
        }

        if (!buffer.hasRemaining()) {
            return read;
        }

        call.newBuffer();

        int currPosition = buffer.position();

        int currLen = call.getReadBufferLen();

        int len = call.getFixLen();

        if (currLen == len) {
            read.success = true;
            return read;
        }

        // 实际读取的字节数
        int readBytes = 0;

        int remain = buffer.limit() - buffer.position(); // buffer 里剩余字节数
        if (currLen == 0) {
            // 说明还没有任何数据
            readBytes = len > remain ? remain : len;

        } else {
            int needBytes = len - currLen; // 还需要的字节数
            readBytes = needBytes > remain ? remain : needBytes;
        }

        ByteBuffer needBuffer = ByteBuffers.getSlice(buffer, currPosition, readBytes); // 返回的是一个新的BUFFER
        call.putBuffer(needBuffer);

        buffer.position(currPosition + readBytes);

        if (call.getReadBufferLen() != len) {
            logger.warn(call.getBlockName() + " buffer has not enough bytes, this block should be " + len
                        + " bytes, but only" + call.getReadBufferLen() + " bytes");
            read.readBytes = readBytes;
            return read;
        }

        call.doSuccess();

        read.readBytes = readBytes;
        read.success = true;
        return read;
    }

    /**
     * 创建报文头对象
     * 
     * @return
     */
    protected UpstreamHeader createUpstreamHeader() {
        return new CommRequestHeader();
    }

    private class ReadDTO {

        boolean     success;

        /**
         * 字符串读取所在位置
         */
        private int currIndex;

        /**
         * 读取的字节数
         */
        private int readBytes;
    }

    private interface CallBack {

        String getBlockName();

        int getFixLen();

        boolean hasRead();

        int getReadBufferLen();

        void putBuffer(ByteBuffer buffer);

        void newBuffer();

        void doSuccess() throws CharacterCodingException;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("aStr=" + this.aBlock).append(", aBytesLen=" + A_FIX_LEN).append(", bStr=" + bBlock).append(", bBytesLen="
                                                                                                                      + B_FIX_LEN);
        sb.append(", cStr=" + cBlock).append(", cBytesLen=" + C_FIX_LEN);

        int totalBytes = A_FIX_LEN + B_FIX_LEN + C_FIX_LEN + dBytesLen + eBytesLen;
        sb.append(", dBytesLen=" + dBytesLen).append(", eBytesLen=" + eBytesLen).append(", totalBytes=" + totalBytes);

        return sb.toString();
    }


    
    

}
