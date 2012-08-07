package com.kongur.monolith.event;

/**
 * 管理监听器和发布事件
 * 
 * @author zhengwei
 * @param <T>
 */
public interface SystemEventMulticaster<E extends SystemEvent> {

    /**
     * 添加监听器
     * 
     * @param listener
     */
    void addListener(SystemListener<E> listener);

    /**
     * 删除监听器
     * 
     * @param listener
     */
    void removeListener(SystemListener<E> listener);

    /**
     * 投递事件
     * 
     * @param event
     */
    void multicastEvent(E event);

}
