package com.kongur.monolith.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 事件投放器
 * 
 * @author zhengwei
 */
public class SimpleEventMulticaster extends AbstractEventMulticaster<Event> {

    /**
     * 异步事件投递用的线程池
     */
    private Executor executor;

    public SimpleEventMulticaster() {
    }

    public SimpleEventMulticaster(Executor executor) {
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
        for (final EventListener<Event> listener : getSystemListeners(event)) {

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
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}
