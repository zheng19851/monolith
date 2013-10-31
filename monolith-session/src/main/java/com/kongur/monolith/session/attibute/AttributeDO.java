package com.kongur.monolith.session.attibute;

/**
 * @author zhengwei
 * @date£º2011-6-15
 */

public class AttributeDO {

    private AttributeConfigDO cookieConfigDO;

    private String            value;

    public AttributeDO(AttributeConfigDO ac, String v) {
        this.cookieConfigDO = ac;
        this.value = v;
    }

    public AttributeConfigDO getAttributeConfigDO() {
        return cookieConfigDO;
    }

    public void setAttributeConfigDO(AttributeConfigDO cookieConfigDO) {
        this.cookieConfigDO = cookieConfigDO;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
