package com.runssnail.monolith.session.store;

import com.runssnail.monolith.session.MonoHttpSession;
import com.runssnail.monolith.session.attibute.AttributeConfigDO;

import java.util.HashMap;
import java.util.Map;

/**
 * 用本地内存实现的SessionAttributeStore，不好用在线上
 * 
 * @author zhengwei
 */
public class MemorySessionAttributeStore extends AbstractSessionAttributeStore {

    private Map<String, Object> attributes = new HashMap<String, Object>();

    @Override
    public Object getAttribute(AttributeConfigDO attribute) {
        return attributes.get(attribute.getKey());
    }

    @Override
    public void setAttribute(AttributeConfigDO attribute, Object value) {
        this.attributes.put(attribute.getClientKey(), value);
    }

    @Override
    public void commit() {

    }

    @Override
    public void init(MonoHttpSession session) {

    }

}
