package com.kongur.monolith.event;

import java.util.EventObject;

/**
 * 事件基类
 * 
 * @author zhengwei
 */
public abstract class Event extends EventObject {

    public Event(Object source) {
        super(source);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -8882808205429352707L;

    @Override
    public String toString() {
        return super.getSource().toString();
    }

}
