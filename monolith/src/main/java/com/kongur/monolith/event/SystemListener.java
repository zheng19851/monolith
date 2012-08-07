package com.kongur.monolith.event;

/**
 * 交易监听接口
 * 
 * @author zhengwei
 */
public interface SystemListener<E extends SystemEvent> {

    /**
     * 处理事件
     * 
     * @param event
     */
    void onEvent(E event);

}
