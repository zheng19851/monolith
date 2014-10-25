package com.runssnail.monolith.event;



/**
 * 主题接口
 * 
 * @author zhengwei
 *
 */
public interface EventSubject<E extends Event> {
    
    void addListener(EventListener<E> listener);
    
    void removeListener(EventListener<E> listener);
    
    void notifyAllListeners(E obj);

}
