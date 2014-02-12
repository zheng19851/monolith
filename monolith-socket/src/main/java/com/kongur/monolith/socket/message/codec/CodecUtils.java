package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.buffer.ByteBuffers;

/**
 * 编码工具类
 * 
 * @author zhengwei
 */
public abstract class CodecUtils {

    public static void putString(ByteBuffer buffer, String value, CharsetEncoder encoder, String splitChar)
                                                                                                           throws CodecException {

        putString(buffer, value, encoder, splitChar, true);
    }

    /**
     * 写buffer
     * 
     * @param buffer
     * @param value
     * @param encoder
     * @param splitChar 分隔符
     * @param appendSplitChar 是否追加分隔符
     * @throws CodecException
     */
    public static void putString(ByteBuffer buffer, String value, CharsetEncoder encoder, String splitChar,
                                 boolean appendSplitChar) throws CodecException {

        if (StringUtil.isNotEmpty(value)) {
            // buffer.putString(value, encoder);
            buffer.put(value.getBytes());
        }

        if (appendSplitChar && splitChar != null) {
            // buffer.putString(splitChar, encoder);
            buffer.put(splitChar.getBytes());
        }
    }

    public static void putString(ByteBuffer buffer, String value, CharsetEncoder encoder) throws CodecException {
        putString(buffer, value, encoder, null, false);
    }

    public static String alignLeft(String value, String defaultStr, int size, String padStr) {

        if (StringUtil.isBlank(value)) {
            value = defaultStr;
        }

        value = StringUtil.alignLeft(value, size, padStr);

        return value;
    }

    public static int getInt(ByteBuffer buffer) throws CodecException {
        return getInt(buffer, Constants.DEFAULT_CHARSET);
    }

    public static int getInt(ByteBuffer buffer, Charset charset) throws CodecException {

        String v = getString(buffer, charset, false);
        return Integer.valueOf(v);
    }

    public static String getString(ByteBuffer buffer) throws CodecException {
        return getString(buffer, Constants.DEFAULT_CHARSET, true);
    }

    public static String getString(ByteBuffer buffer, Charset charset) throws CodecException {
        return getString(buffer, charset, true);
    }

    public static String getString(ByteBuffer buffer, boolean trim) throws CodecException {
        return getString(buffer, Constants.DEFAULT_CHARSET, trim);
    }

    
    /**
     * 转成string
     * 
     * @param buffer ByteBuffer
     * @param charset
     * @param trim 是否去掉前后空格
     * @return
     * @throws CodecException
     */
    public static String getString(ByteBuffer buffer, Charset charset, boolean trim) throws CodecException {

        if (buffer.limit() == 0) {
            return null;
        }

        String v = null;

        byte[] b = new byte[buffer.limit()];

        buffer.get(b);

        v = new String(b, charset);

        if (!trim) {
            return v;
        }

        return StringUtil.trim(v);
    }

    public static String getString(ByteBuffer buffer, int startIndex, int size, CharsetDecoder decoder)
                                                                                                       throws CodecException {
        return getString(buffer, startIndex, size, decoder, true);
    }

    /**
     * 从buffer中解析字段值
     * 
     * @param buffer
     * @param startIndex 起始位置
     * @param endIndex 结束位置
     * @param decoder
     * @param trim 是否去掉前后空格
     * @return
     * @throws CodecException
     */
    public static String getString(ByteBuffer buffer, int startIndex, int len, CharsetDecoder decoder, boolean trim)
                                                                                                                    throws CodecException {
        ByteBuffer dataBuffer = ByteBuffers.getSlice(buffer, startIndex, len);

        // byte[] dst = new byte[len];
        // buffer.get(dst, startIndex, len);
        // ByteBuffer dataBuffer = ByteBuffer.allocate(len);
        // dataBuffer.put(dst);
        // dataBuffer.flip();
        String v = getString(dataBuffer, Constants.DEFAULT_CHARSET, trim);

        return v;
    }

    public static Long getLong(ByteBuffer buffer, int startIdx, int size, CharsetDecoder decoder) throws CodecException {

        String v = getString(buffer, startIdx, size, decoder, false);
        if (StringUtil.isNotBlank(v)) {
            return Long.valueOf(v);
        }

        return null;
    }

    /**
     * 编码成buffer, 左对齐
     * 
     * @param value
     * @param size 字节数
     * @param encoder
     * @return
     */
    public static ByteBuffer getBufferAlignLeft(String value, int size, CharsetEncoder encoder, byte padByte) {

        byte[] b = getAndCheckBytes(value, padByte, size);

        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(b);
        for (int i = 0; i < (size - b.length); i++) {
            buffer.put(padByte);
        }

        buffer.flip();
        return buffer;
    }

    /**
     * 编码成buffer, 左对齐
     * 
     * @param value
     * @param size 字节数
     * @param encoder
     * @return
     */
    public static ByteBuffer getBufferAlignLeft(String value, int size, CharsetEncoder encoder) {

        return getBufferAlignLeft(value, size, encoder, Constants.DEFAULT_BLANK_SPACE_BYTE);
    }

    /**
     * 编码成buffer, 右对齐
     * 
     * @param value
     * @param size 字节数
     * @param encoder
     * @return
     */
    public static ByteBuffer getBufferAlignRight(String value, int size, CharsetEncoder encoder, byte padByte) {

        byte[] b = getAndCheckBytes(value, padByte, size);

        ByteBuffer buffer = ByteBuffer.allocate(size);

        for (int i = 0; i < (size - b.length); i++) {
            buffer.put(padByte);
        }

        buffer.put(b);

        buffer.flip();
        return buffer;
    }

    /**
     * 获取字节 并交验长度，超长就报错
     * 
     * @param value
     * @param defaultByte
     * @param size
     * @return
     */
    private static byte[] getAndCheckBytes(String value, byte defaultByte, int size) {

        byte[] b = null;

        if (value == null) {
            b = new byte[] { defaultByte };
        } else {
            b = value.getBytes(Constants.DEFAULT_CHARSET);
        }

        check(value, b.length, size);

        return b;
    }

    public static ByteBuffer getBufferAlignRight(String v, int size, CharsetEncoder encoder) {
        return getBufferAlignRight(v, size, encoder, Constants.ZERO_BYTE);
    }

    private static void check(String value, int realLen, int size) {

        if (realLen > size) {
            throw new CodecException("the field bytes length is too long! value=" + value + ", shold be " + size
                                     + ", but " + realLen);
        }
    }

    public static ByteBuffer getLongBuffer(Long v, int size, CharsetEncoder encoder) {
        if (v == null) {
            v = 0L;
        }
        return getBufferAlignRight(String.valueOf(v), size, encoder, Constants.ZERO_BYTE);
    }

    public static byte[] getBytes(String v) {
        if (v != null) {
            return v.getBytes(Constants.DEFAULT_CHARSET);
        }

        return new byte[0];
    }

    public static byte[] getBytes(Long v) {
        if (v != null) {
            return v.toString().getBytes(Constants.DEFAULT_CHARSET);
        }

        return new byte[0];
    }

    public static Integer getInt(ByteBuffer buffer, int startIndex, int size, CharsetDecoder decoder)
                                                                                                     throws CodecException {
        String v = getString(buffer, startIndex, size, decoder);

        if (v == null || "".equals(v)) {
            return 0;
        }

        return Integer.valueOf(v);
    }

    public static ByteBuffer getIntBuffer(Integer v, int len, CharsetEncoder encoder) {

        if (v == null) {
            v = 0;
        }

        return getBufferAlignRight(String.valueOf(v), len, encoder);
    }

}
