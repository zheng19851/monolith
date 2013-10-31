package com.kongur.monolith.session.attibute;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author zhengwei
 */
public abstract class AbstractAttributesConfigManager implements AttributesConfigManager {

    protected final Logger                   log              = Logger.getLogger(getClass());

    protected Map<String, AttributeConfigDO> attributeConfigs = new HashMap<String, AttributeConfigDO>();

    protected Map<String, AttributeConfigDO> getAttributeConfigs() {
        return attributeConfigs;
    }

}
