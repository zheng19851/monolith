package com.kongur.monolith.weixin.core.domain.message;

/**
 * 事件消息类型
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public enum EnumEventType {

    SUBSCRIBE("subscribe", "关注事件"),

    UNSUBSCRIBE("unsubscribe", "取消关注事件"),

    SCAN("SCAN", "二维码扫描-用户已关注事件"),

    LOCATION("LOCATION", "上报地理位置事件"),

    CLICK("CLICK", "自定义菜单事件")

    ;

    private EnumEventType(String value, String desc) {
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

    /**
     * 是否关注事件
     * 
     * @param evenType
     * @return
     */
    public static boolean isSubscribe(String evenType) {
        return SUBSCRIBE.getValue().equalsIgnoreCase(evenType);
    }

    /**
     * 是否取消关注事件
     * 
     * @param eventType
     * @return
     */
    public static boolean isUnSubscribe(String eventType) {
        return UNSUBSCRIBE.getValue().equalsIgnoreCase(eventType);
    }

    /**
     * 是否上报地理位置事件
     * 
     * @param eventType
     * @return
     */
    public static boolean isLocation(String eventType) {
        return LOCATION.getValue().equalsIgnoreCase(eventType);
    }

    /**
     * 自定义菜单事件
     * 
     * @param eventType
     * @return
     */
    public static boolean isClick(String eventType) {
        return CLICK.getValue().equalsIgnoreCase(eventType);
    }

    /**
     * 扫描二维码时，用户已关注
     * 
     * @param eventType
     * @return
     */
    public static boolean isScan(String eventType) {
        return false;
    }

}
