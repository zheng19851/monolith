package com.kongur.monolith.event;



/**
 * 主题接口
 * 
 * @author zhengwei
 *
 */
public interface SystemSubject<E extends SystemEvent> {
    
    void addListener(SystemListener<E> listener);
    
    void removeListener(SystemListener<E> listener);
    
    void notifyAllListeners(E obj);

}
