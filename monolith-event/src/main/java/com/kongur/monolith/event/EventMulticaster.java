package com.kongur.monolith.event;

/**
 * 管理监听器和发布事件
 * 
 * @author zhengwei
 * @param <T>
 */
public interface EventMulticaster<E extends Event> {

    /**
     * 添加监听器
     * 
     * @param listener
     */
    void addListener(EventListener<E> listener);

    /**
     * 删除监听器
     * 
     * @param listener
     */
    void removeListener(EventListener<E> listener);

    /**
     * 投递事件
     * 
     * @param event
     */
    void multicastEvent(E event);

}
