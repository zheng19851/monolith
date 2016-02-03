package com.runssnail.monolith.session.attibute;

import java.io.Serializable;

/**
 * @author zhengwei
 * @date£º2011-6-15
 */

public class AttributeDO implements Serializable {

    private static final long serialVersionUID = 5857808752611003260L;

    private AttributeConfigDO attributeConfigDO;

    private String            value;

    public AttributeDO(AttributeConfigDO ac, String v) {
        this.attributeConfigDO = ac;
        this.value = v;
    }

    public AttributeConfigDO getAttributeConfigDO() {
        return attributeConfigDO;
    }

    public void setAttributeConfigDO(AttributeConfigDO cookieConfigDO) {
        this.attributeConfigDO = cookieConfigDO;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AttributeDO{" +
                "attributeConfigDO=" + attributeConfigDO +
                ", value='" + value + '\'' +
                '}';
    }
}
