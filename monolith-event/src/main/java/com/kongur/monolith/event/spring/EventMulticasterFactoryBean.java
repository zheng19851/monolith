package com.kongur.monolith.event.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.kongur.monolith.event.DefaultEventMulticaster;
import com.kongur.monolith.event.Event;
import com.kongur.monolith.event.EventMulticaster;
import com.kongur.monolith.event.EventListener;

/**
 * 创建SystemEventMulticaster
 * 
 * @author zhengwei
 */
public class EventMulticasterFactoryBean implements FactoryBean<EventMulticaster<Event>>, ApplicationContextAware, InitializingBean {

    private final Logger                        logger      = Logger.getLogger(getClass());

    /**
     * 具体的SystemEventMulticaster实例类型
     */
    private Class<?>                            clazz;

    private EventMulticaster<Event> systemEventMulticaster;

    /**
     * 配置的监听器
     */
    private List<EventListener<Event>>   listeners;

    /**
     * 是否自动收集监听器，如果为true, 那么会从容器中自动获取类型为SystemListener的所有监听器，并注册到SystemEventMulticaster里面，默认为true
     */
    private boolean                             autoCollect = true;

    private ApplicationContext                  applicationContext;

    @Override
    public EventMulticaster<Event> getObject() throws Exception {
        return systemEventMulticaster;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setClazz(Class<EventMulticaster<Event>> clazz) {
        this.clazz = clazz;
    }

    public List<EventListener<Event>> getListeners() {
        return listeners;
    }

    public void setListeners(List<EventListener<Event>> listeners) {
        this.listeners = listeners;
    }

    public boolean isAutoCollect() {
        return autoCollect;
    }

    public void setAutoCollect(boolean autoCollect) {
        this.autoCollect = autoCollect;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void afterPropertiesSet() throws Exception {
        if (clazz == null) {
            // throw new RuntimeException("please set the property of 'clazz', type of SystemEventMulticaster");
            logger.warn("the property of clazz has not be setted, the default value will be setted[SimpleSystemEventMulticaster.class]!");
            clazz = DefaultEventMulticaster.class;
        }

        systemEventMulticaster = (EventMulticaster<Event>) clazz.newInstance();

        if (listeners != null) {
            for (EventListener<Event> listener : listeners) {
                systemEventMulticaster.addListener(listener);
            }
        }

        if (autoCollect) {
            Map<String, EventListener> listeners = applicationContext.getBeansOfType(EventListener.class);

            if (logger.isDebugEnabled()) {
                logger.debug("auto-collect find " + (listeners != null ? listeners.size() : 0) + " listeners.");
            }

            if (listeners != null && !listeners.isEmpty()) {
                for (Entry<String, EventListener> entry : listeners.entrySet()) {
                    systemEventMulticaster.addListener(entry.getValue());
                }
            }

        }
    }

}
