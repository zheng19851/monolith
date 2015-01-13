package com.runssnail.monolith.event.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.runssnail.monolith.event.DefaultEventMulticaster;
import com.runssnail.monolith.event.Event;
import com.runssnail.monolith.event.EventListener;
import com.runssnail.monolith.event.EventMulticaster;

/**
 * ����EventMulticaster
 * 
 * @author zhengwei
 */
public class EventMulticasterFactoryBean implements FactoryBean<EventMulticaster<Event>>, ApplicationContextAware, InitializingBean {

    private final Logger                        logger      = Logger.getLogger(getClass());

    /**
     * �����EventMulticasterʵ������
     */
    private Class<?>                            clazz;

    /**
     * �¼�������
     */
    private EventMulticaster<Event> eventMulticaster;

    /**
     * ���õļ�����
     */
    private List<EventListener<Event>>   listeners;

    /**
     * �Ƿ��Զ��ռ������������Ϊtrue, ��ô����������Զ���ȡ����ΪEventListener�����м���������ע�ᵽEventMulticaster���棬Ĭ��Ϊtrue
     */
    private boolean                             autoCollect = true;

    private ApplicationContext                  applicationContext;

    @Override
    public EventMulticaster<Event> getObject() throws Exception {
        return eventMulticaster;
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
            logger.warn("the property of clazz has not be setted, the default value will be setted[DefaultEventMulticaster.class]!");
            clazz = DefaultEventMulticaster.class;
        }

        eventMulticaster = (EventMulticaster<Event>) clazz.newInstance();

        if (listeners != null) {
            for (EventListener<Event> listener : listeners) {
                eventMulticaster.addListener(listener);
            }
        }

        if (autoCollect) {
            Map<String, EventListener> listeners = applicationContext.getBeansOfType(EventListener.class);

            if (logger.isDebugEnabled()) {
                logger.debug("auto-collect find " + (listeners != null ? listeners.size() : 0) + " listeners.");
            }

            if (listeners != null && !listeners.isEmpty()) {
                for (Entry<String, EventListener> entry : listeners.entrySet()) {
                    eventMulticaster.addListener(entry.getValue());
                }
            }

        }
    }

}