package com.kongur.monolith.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 事件投放器
 * 
 * @author zhengwei
 */
public class DefaultEventMulticaster extends AbstractEventMulticaster<Event> {

    /**
     * 异步事件投递用的线程池
     */
    private Executor executor;

    public DefaultEventMulticaster() {
    }

    public DefaultEventMulticaster(Executor executor) {
        this.executor = executor;
    }

    /**
     * 初始化
     */
    public void init() {
        if (this.executor == null) {
            this.executor = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void multicastEvent(final Event event) {
        for (final EventListener<Event> listener : getEventListeners(event)) {
            try {
                exeListener(listener, event);
            } catch (Exception e) {
                log.error("execute listener error, event=" + event + ", listener=" + listener, e);
            }

        }
    }

    private void exeListener(final EventListener<Event> listener, final Event event) {

        if (executor != null) {

            executor.execute(new Runnable() {

                public void run() {
                    try {
                        listener.onEvent(event);
                    } catch (Exception e) {
                        log.error("multicast event error", e);
                    }
                }
            });

        } else {
            listener.onEvent(event);
        }

    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}
