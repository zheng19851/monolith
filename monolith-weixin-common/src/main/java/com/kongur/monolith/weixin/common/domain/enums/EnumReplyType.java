package com.kongur.monolith.weixin.common.domain.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回复消息或主动发送消息类型
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
public enum EnumReplyType {

    TEXT("text", "文本消息"), IMAGE("image", "图片消息"), VOICE("voice", "语音消息"), VEDIO("vedio", "视频消息"),
    MUSIC("music", "音乐消息"), NEWS("news", "图文消息")

    ;

    private EnumReplyType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String                                  value;
    private String                                  desc;

    private static final Map<String, EnumReplyType> CACHE        = new HashMap<String, EnumReplyType>(values().length);
    // 目前可用的类型
    private static final List<EnumReplyType>        USABLE_TYPES = new ArrayList<EnumReplyType>(values().length);

    static {
        USABLE_TYPES.add(TEXT);
        // USABLE_TYPES.add(IMAGE);
        // USABLE_TYPES.add(VOICE);
        USABLE_TYPES.add(NEWS); // 图文

        for (EnumReplyType entry : values()) {
            CACHE.put(entry.getValue(), entry);
        }

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isText(String replyType) {
        return TEXT.getValue().equals(replyType);
    }

    public static boolean isImage(String replyType) {
        return IMAGE.getValue().equals(replyType);
    }

    public static boolean isVoice(String replyType) {
        return VOICE.getValue().equals(replyType);
    }

    public static boolean isVedio(String replyType) {
        return VEDIO.getValue().equals(replyType);
    }

    public static boolean isMusic(String replyType) {
        return MUSIC.getValue().equals(replyType);
    }

    public static boolean isNews(String replyType) {
        return NEWS.getValue().equals(replyType);
    }

    /**
     * 当前可用的类型
     * 
     * @return
     */
    public static List<EnumReplyType> getUsableTypes() {
        return USABLE_TYPES;
    }

    public static String getDescByValue(String type) {
        EnumReplyType reply = getByValue(type);

        return reply != null ? reply.getDesc() : null;
    }

    private static EnumReplyType getByValue(String type) {
        return CACHE.get(type);
    }
}
