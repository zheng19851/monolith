package com.runssnail.monolith.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 事件投放器, 提供添加、删除、发布事件功能
 * 
 * @author zhengwei
 */

public abstract class AbstractSystemEventMulticaster<E extends SystemEvent> implements SystemEventMulticaster<E> {

    protected final Logger                           logger           = Logger.getLogger(getClass());

    /**
     * 存放所有监听器
     */
    private final ListenerRetriever                  defaultRetriever = new ListenerRetriever();

    /**
     * key 为事件class类型 + 事件数据class类型, value为监听器集合
     */
    private final Map<ListenerCacheKey, ListenerRetriever> listenerCache    = new ConcurrentHashMap<ListenerCacheKey, ListenerRetriever>();

    @Override
    public void addListener(SystemListener<E> listener) {
        synchronized (this.defaultRetriever) {
            this.defaultRetriever.applicationListeners.add(listener);
            this.listenerCache.clear();
        }
    }

    @Override
    public void removeListener(SystemListener<E> listener) {
        synchronized (this.defaultRetriever) {
            this.defaultRetriever.applicationListeners.remove(listener);
            this.listenerCache.clear();
        }
    }

    /**
     * 获取和当前事件匹配的监听器
     * 
     * @param event
     * @return
     */
    protected Collection<SystemListener<E>> getSystemListeners(E event) {

        ListenerCacheKey cacheKey = createCacheKey(event);

        ListenerRetriever retriever = this.listenerCache.get(cacheKey);

        if (retriever != null) {
            return retriever.applicationListeners;
        }

        retriever = new ListenerRetriever();
        List<SystemListener<E>> allListeners = new ArrayList<SystemListener<E>>();

        synchronized (this.defaultRetriever) {
            for (SystemListener<E> listener : this.defaultRetriever.applicationListeners) {
                if (supportsEvent(listener, event)) {
                    retriever.applicationListeners.add(listener);
                    allListeners.add(listener);
                }
            }

            this.listenerCache.put(cacheKey, retriever);
        }

        return allListeners;
    }

    /**
     * 判断监听器是否支持当前提供的事件类型
     * 
     * @param listener
     * @param event
     * @return
     */
    protected boolean supportsEvent(SystemListener<E> listener, E event) {
        Class<? extends SystemEvent> eventType = event.getClass();
        Class<?> sourceType = event.getSource().getClass();

        SmartSystemListener<E> smartListener = (listener instanceof SmartSystemListener ? (SmartSystemListener<E>) listener : new SmartSystemListenerAdapter<E>(
                                                                                                                                                       listener));
        return (smartListener.supportsEventType(eventType) && smartListener.supportsSourceType(sourceType));
    }

    /**
     * 创建 缓存KEY
     * 
     * @param event
     * @return
     */
    protected ListenerCacheKey createCacheKey(E event) {
        Class<? extends SystemEvent> eventType = event.getClass();
        Class<?> sourceType = event.getSource().getClass();
        return new ListenerCacheKey(eventType, sourceType);
    }

    /**
     * @author zhengwei
     */
    private class ListenerRetriever {

        public final Set<SystemListener<E>> applicationListeners;

        public ListenerRetriever() {
            this.applicationListeners = new LinkedHashSet<SystemListener<E>>();
        }

    }

    public Map<ListenerCacheKey, ListenerRetriever> getListenerCache() {
        return listenerCache;
    }

}
