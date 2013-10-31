package com.kongur.monolith.session.attibute;

import java.util.Collection;

import com.kongur.monolith.session.Lifecycle;


/**
 * 
 * cookie 配置管理器
 * 
 * 可通过spring配置，也可以从XML文件里读取等
 * 
 * @author zhengwei
 * @date：2011-6-15
 * 
 */
public interface AttributesConfigManager extends Lifecycle {
    
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
