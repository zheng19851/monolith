package com.runssnail.monolith.event;

import java.io.Serializable;

/**
 * ¼àÌýÆ÷»º´æ key
 * 
 * @author zhengwei
 */
@SuppressWarnings("rawtypes")
public class ListenerCacheKey implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7680806386519074186L;

    private Class             eventType;

    private Class             sourceType;

    public ListenerCacheKey(Class eventType, Class sourceType) {
        this.eventType = eventType;
        this.sourceType = sourceType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof ListenerCacheKey) {
            ListenerCacheKey otherKey = (ListenerCacheKey) other;
            return (this.eventType.equals(otherKey.eventType) && this.sourceType.equals(otherKey.sourceType));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.eventType.hashCode() * 29 + this.sourceType.hashCode();
    }

}
