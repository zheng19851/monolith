package com.runssnail.monolith.session.store;

import com.runssnail.monolith.session.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwei on 2016/2/16.
 */
public class DefaultSessionStoreFactory implements SessionStoreFactory {

    private Map<String, Class<? extends SessionStore>> storeTypeMap;

    @Override
    public SessionStore create(String name) {
        Class<? extends SessionStore> storeClass = this.storeTypeMap.get(name);

        SessionStore store = ClassUtils.newInstance(storeClass);
        return store;
    }

    public void setStoreTypeMap(Map<String, Class<? extends SessionStore>> storeTypeMap) {
        this.storeTypeMap = storeTypeMap;
    }

    public Map<String, Class<? extends SessionStore>> getStoreTypeMap() {
        return storeTypeMap;
    }

    /**
     * 生成一组新的store
     *
     * @return
     */
    public Map<String, SessionStore> createStores() {
        Map<String, SessionStore> result = new HashMap<String, SessionStore>(storeTypeMap.size());
        for (Map.Entry<String, Class<? extends SessionStore>> entry : storeTypeMap.entrySet()) {
            String storeName = entry.getKey();
            Class<? extends SessionStore> storeClass = entry.getValue();

            SessionStore store = ClassUtils.newInstance(storeClass);
            result.put(storeName, store);
        }
        return result;
    }

}
