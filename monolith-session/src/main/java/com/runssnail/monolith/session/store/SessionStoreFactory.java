package com.runssnail.monolith.session.store;

import java.util.Map;

/**
 * Created by zhengwei on 2016/2/16.
 */
public interface SessionStoreFactory {

    SessionStore create(String name);

    Map<String, SessionStore> createStores();
}
