package com.kongur.monolith.weixin.core.domain.message;

/**
 * 消息类型
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public enum EnumMessageType {

    // 开发者认证
    DEVELOPER_VALIDATE("dvm", "开发者认证消息"),

    // 普通消息
    TEXT("text", "文本消息"), 
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"), 
    VIDEO("video", "视频消息"), 
    LOCATION("location","地理位置消息"),
    LINK("link", "链接消息"),

    // 事件推送消息
    EVENT("event", "事件消息"),

    // VOICE_RECOGNITION("v_r", "语音识别消息")

    ;

    private EnumMessageType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 消息类型的值
     */
    private String value;

    /**
     * 消息描述
     */
    private String desc;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static boolean isDeveloperValidate(String msgType) {
        return DEVELOPER_VALIDATE.getValue().equalsIgnoreCase(msgType);
    }

    /**
     * 是否文本消息
     * 
     * @param msgType
     * @return
     */
    public static boolean isText(String msgType) {
        return TEXT.getValue().equalsIgnoreCase(msgType);
    }

    /**
     * 是否事件消息
     * 
     * @param msgType
     * @return
     */
    public static boolean isEvent(String msgType) {
        return EVENT.getValue().equalsIgnoreCase(msgType);
    }

    /**
     * 语言类类型消息
     * 
     * @param msgType
     * @return
     */
    public static boolean isVoice(String msgType) {
        return VOICE.getValue().equalsIgnoreCase(msgType);
    }

    /**
     * 图片消息
     * 
     * @param msgType
     * @return
     */
    public static boolean isImage(String msgType) {
        return IMAGE.getValue().equalsIgnoreCase(msgType);
    }

    /**
     * 视频消息
     * 
     * @param msgType
     * @return
     */
    public static boolean isVideo(String msgType) {
        return VIDEO.getValue().equalsIgnoreCase(msgType);
    }

    public static boolean isLocation(String msgType) {
        return LOCATION.getValue().equalsIgnoreCase(msgType);
    }

    public static boolean isLink(String msgType) {
        return LINK.getValue().equalsIgnoreCase(msgType);
    }

}
