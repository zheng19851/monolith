package com.runssnail.monolith.session.store;

import com.runssnail.monolith.session.MonoHttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zhengwei
 */
public abstract class AbstractSessionStore implements SessionStore {

    protected final Log log = LogFactory.getLog(getClass());

    protected MonoHttpSession session;

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

    @Override
    public void init(MonoHttpSession session) {
        this.session = session;
    }

}
