package com.runssnail.monolith.session.attibute;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * abstract AttributesConfigManager
 *
 * @author zhengwei
 */
public abstract class AbstractAttributesConfigManager implements AttributesConfigManager {

    protected final Logger                   log              = Logger.getLogger(getClass());

    /**
     * 所有属性配置
     */
    protected Map<String, AttributeConfigDO> attributeConfigs = new HashMap<String, AttributeConfigDO>();

    protected Map<String, AttributeConfigDO> getAttributeConfigs() {
        return attributeConfigs;
    }

    public void setAttributeConfigs(Map<String, AttributeConfigDO> attributeConfigs) {
        this.attributeConfigs = attributeConfigs;
    }

    @Override
    public AttributeConfigDO getAttributeConfigDO(String cookieName) {
        return attributeConfigs.get(cookieName);
    }

    @Override
    public Collection<String> getAttributeNames() {
        return attributeConfigs.keySet();
    }

    /**
     * 添加属性
     * 
     * @param attrConf
     */
    public void addAttributeConfig(AttributeConfigDO attrConf) {
        if (attributeConfigs == null) {
            this.attributeConfigs = new HashMap<String, AttributeConfigDO>();
        }

        this.attributeConfigs.put(attrConf.getKey(), attrConf);
    }

    /**
     * 初始化AttributesConfigManager
     */
    @Override
    public void init() {

        if (this.attributeConfigs == null) {
            if (log.isDebugEnabled()) {
                log.debug("there are not attributes config");
            }

        } else {
            if (log.isDebugEnabled()) {
                log.debug("current attributes config, " + this.attributeConfigs);
            }
        }

    }

    @Override
    public void destroy() {
        // ignore

    }

}
