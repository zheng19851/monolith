package com.kongur.monolith.session;

import org.apache.log4j.Logger;

/**
 * @author zhengwei
 */
public abstract class AbstractSessionAttributeStore implements SessionAttributeStore {

    protected final Logger    log = Logger.getLogger(getClass());

    protected MonoHttpSession monoHttpSession;

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(MonoHttpSession session) {
        this.monoHttpSession = session;
    }

}
