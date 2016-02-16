package com.runssnail.monolith.session.store;

import com.runssnail.monolith.session.attibute.AttributeConfigDO;
import org.springframework.data.redis.core.RedisOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwei on 2016/2/16.
 */
public class RedisSessionStore extends AbstractSessionStore {

    private final RedisOperations<String, Object> sessionRedisOperations;

    /**
     * 由于有两条线程(一条是请求线程，另一条是线程池中的线程)同时访问以下属性，所以这些属性都需要加上volatile修饰
     */
    private volatile Map<String, Object> attributes = new HashMap<String, Object>();

    public RedisSessionStore(RedisOperations<String, Object> sessionRedisOperations) {
        this.sessionRedisOperations = sessionRedisOperations;
    }

    @Override
    public Object getAttribute(AttributeConfigDO attribute) {
        return attributes.get(attribute.getKey());
    }

    @Override
    public void setAttribute(AttributeConfigDO attribute, Object value) {
        String key = attribute.getKey();
        if (value != null) {
            attributes.put(key, value);
        } else {
            attributes.remove(key);
        }
    }

    @Override
    public void commit() {

    }

    @Override
    public void onSessionReady() {
        // read from redis
    }

}
