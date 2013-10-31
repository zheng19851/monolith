package com.kongur.monolith.session;

import com.kongur.monolith.session.attibute.AttributeConfigDO;


/**
 * session数据存储
 * 
 * @author zhengwei
 * @date：2011-6-15
 */

public interface SessionAttributeStore extends Lifecycle {

    /**
     * 获取属性值
     * 
     * @param attribute
     * @return
     */
    public Object getAttribute(AttributeConfigDO attribute);

   /**
    * 设置属性值
    * 
    * @param attribute
    * @param value
    */
    public void setAttribute(AttributeConfigDO attribute, Object value);

    /**
     * 提交修改
     */
    public void commit();

    /**
     * 初始化
     * 
     * @param session
     */
    public void init(MonoHttpSession session);


}
