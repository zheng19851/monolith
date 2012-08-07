package com.kongur.monolith.event;


/**
 * 支持监听器根据事件类型和数据类型匹配事件
 * 
 * @author zhengwei
 *
 */
public interface SmartSystemListener<E extends SystemEvent> extends SystemListener<E> {

    /**
     * 是否支持给定的事件类型
     */
    boolean supportsEventType(Class<? extends SystemEvent> eventType);

    /**
     * 是否支持给定的原类型
     */
    boolean supportsSourceType(Class<?> sourceType);

}
