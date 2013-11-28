package com.kongur.monolith.event;

/**
 * 监听器接口
 * 
 * @author zhengwei
 */
public interface EventListener<E extends Event> {

    /**
     * 处理事件
     * 
     * @param event
     */
    void onEvent(E event);

}
