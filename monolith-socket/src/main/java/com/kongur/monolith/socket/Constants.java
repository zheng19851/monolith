package com.kongur.monolith.socket;

import java.nio.charset.Charset;

/**
 * 编解码时用到的常量
 * 
 * @author zhengwei
 */
public class Constants {

    private Constants() {

    }

    public static final Charset DEFAULT_CHARSET                = Charset.forName("GBK");

    /**
     * 换行符
     */
    public static final String  DEFAULT_NEW_BREAK              = "\n";

    /**
     * 回车换行
     */
    public static final String  DEFAULT_ENTER_NEW_BREAK        = "\r\n";

    public static final byte    DEFAULT_NEW_BREAK_BYTE         = DEFAULT_NEW_BREAK.getBytes(DEFAULT_CHARSET)[0];

    /**
     * 默认的分割字节
     */
    public static final byte    DEFAULT_SPLIT_BYTE             = 0x02;

    /**
     * 分割符
     */
    public static final String  DEFAULT_SPLIT_CHAR             = new String(new byte[] { DEFAULT_SPLIT_BYTE },
                                                                            DEFAULT_CHARSET);

    /**
     * 设置到session里的对象key
     */
    public static final String  RESPONSE_KEY                   = "response_key";

    /**
     * 空格
     */
    public static final String  BLANK_SPACE                    = " ";

    /**
     * 空格转换成字节
     */
    public static final byte    DEFAULT_BLANK_SPACE_BYTE       = BLANK_SPACE.getBytes(DEFAULT_CHARSET)[0];

    /**
     * spring初始化时，自动将相关的codec设置到属性里，为了区别ID，后缀加上MessageCodec
     */
    public static final String  MESSAGE_CODEC                  = "MessageCodec";

    public static final String  ZERO                           = "0";

    /**
     * 零换成字节
     */
    public static final byte    ZERO_BYTE                      = ZERO.getBytes(DEFAULT_CHARSET)[0];

    /**
     * 默认的日期格式
     */
    public static final String  DEFAULT_DATE_FORMAT_STR        = "yyyyMMdd";

    /**
     * 默认的时间格式
     */
    public static final String  DEFAULT_TIME_FORMAT_STR        = "HHmmss";

    /**
     * 默认的创建buffer大小
     */
    public static final int     DEFAULT_BUFFER_SIZE            = 256;

    public static final String  MESSAGE_ENCODER                = "MessageEncoder";

    public static final String  MESSAGE_DECODER                = "MessageDecoder";

}
