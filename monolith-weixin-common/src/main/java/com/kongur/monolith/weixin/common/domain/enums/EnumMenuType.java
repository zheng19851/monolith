package com.kongur.monolith.weixin.common.domain.enums;

/**
 * 菜单类型
 * 
 * @author zhengwei
 * @date 2014年2月20日
 */
public enum EnumMenuType {

    CLICK("click", "事件类型"), VIEW("view", "链接类型")

    ;

    private EnumMenuType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;
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

    public static boolean isView(String val) {
        return VIEW.getValue().equalsIgnoreCase(val);
    }

    public static boolean isClick(String val) {
        return CLICK.getValue().equalsIgnoreCase(val);
    }

}
