package com.runssnail.monolith.session.store;

import org.apache.log4j.Logger;

/**
 * @author zhengwei
 */
public abstract class AbstractSessionAttributeStore implements SessionAttributeStore {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("init...");
        }
    }

    @Override
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("destroy...");
        }
    }

}
