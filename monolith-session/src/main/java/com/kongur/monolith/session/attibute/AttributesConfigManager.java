package com.kongur.monolith.session.attibute;

import java.util.Collection;


/**
 * 
 * cookie 配置管理器
 * 
 * @author zhengwei
 * @date：2011-6-15
 * 
 */
public interface AttributesConfigManager {
    
    /**
     * 根据属性名获取属性配置
     * 
     * @param attributeName
     * @return
     */
    AttributeConfigDO getAttributeConfigDO(String attributeName);

    /**
     * 获取当前所有属性名
     * 
     * @return
     */
    Collection<String> getAttributeNames();
    
}
