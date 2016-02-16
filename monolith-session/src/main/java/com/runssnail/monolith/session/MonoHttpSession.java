package com.runssnail.monolith.session;

import com.runssnail.monolith.lang.UniqID;
import com.runssnail.monolith.session.attibute.AttributeConfigDO;
import com.runssnail.monolith.session.attibute.AttributesConfigManager;
import com.runssnail.monolith.session.store.SessionStore;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.*;

/**
 * mono http session 实现
 *
 * @author zhengwei
 * @date：2011-6-15
 */

public class MonoHttpSession implements HttpSession, Lifecycle {

    private static final Log log = LogFactory.getLog(MonoHttpSession.class);

    public static final String SESSION_ID = "sessionID";

    /**
     *
     */
    private MonoHttpServletRequest request;

    private MonoHttpServletResponse response;

    private volatile String sessionId;

    private volatile ServletContext context;

    /**
     * session 创建时间
     */
    private long creationTime = System.currentTimeMillis();

    private long lastAccessedTime = creationTime;

    /**
     * session失效时间，默认半个小时,单位：秒
     */
    private int maxInactiveInterval = 1800;

    /**
     * 所有SessionAttributeStore
     */
    private Map<String, SessionStore> stores;

    /**
     * 属性信息管理
     */
    private AttributesConfigManager attributesConfigManager;

    public MonoHttpSession() {

    }

    /**
     * @param monoRequest             MonoHttpServletRequest
     * @param monoResponse            MonoHttpServletResponse
     * @param context                 ServletContext
     * @param stores                  所有SessionStore
     * @param attributesConfigManager AttributesConfigManager
     */
    public MonoHttpSession(MonoHttpServletRequest monoRequest, MonoHttpServletResponse monoResponse, ServletContext context,
                           Map<String, SessionStore> stores, AttributesConfigManager attributesConfigManager) {
        this.request = monoRequest;
        this.response = monoResponse;
        this.context = context;
        this.stores = stores;
        this.attributesConfigManager = attributesConfigManager;
    }

    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public String getId() {
        return this.sessionId;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return this.context;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(String name) {

        return getAttributeFromStore(name);
    }

    /**
     * 从store里取
     *
     * @param name
     * @return
     */
    private Object getAttributeFromStore(String name) {

        return processAttribute(name, new Callback() {

            @Override
            public Object doCallback(SessionStore store, AttributeConfigDO ac) {
                return store.getAttribute(ac);
            }

        });

    }

    /**
     * @param attrName 内部属性名
     * @param callback
     * @return
     */
    private Object processAttribute(String attrName, Callback callback) {
        AttributeConfigDO attributeConfigDO = getAttributeConfigDO(attrName);
        if (attributeConfigDO == null) {
            return null;
        }

        SessionStore store = resolveStore(attributeConfigDO);
        return callback.doCallback(store, attributeConfigDO);
    }

    /**
     * 决定store
     *
     * @param attrConfig
     * @return
     */
    private SessionStore resolveStore(AttributeConfigDO attrConfig) {
        return this.stores.get(attrConfig.getStoreKey());
    }

    /**
     * 根据属性名称获取配置信息
     */
    private AttributeConfigDO getAttributeConfigDO(String name) {
        return attributesConfigManager.getAttributeConfigDO(name);
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Collection<String> names = attributesConfigManager.getAttributeNames();
        return Collections.enumeration(names);
    }

    @Override
    public String[] getValueNames() {
        Collection<String> names = attributesConfigManager.getAttributeNames();
        return names.toArray(new String[names.size()]);
    }

    @Override
    public void setAttribute(String name, final Object value) {

        processAttribute(name, new Callback() {

            @Override
            public Object doCallback(SessionStore store, AttributeConfigDO ac) {
                store.setAttribute(ac, value);
                return null;
            }
        });

    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        setAttribute(name, null);
    }

    @Override
    public void removeValue(String name) {
        setAttribute(name, null);
    }

    @Override
    public void invalidate() {
        Collection<String> names = attributesConfigManager.getAttributeNames();
        for (String name : names) {
            setAttribute(name, null);
        }
    }

    @Override
    public boolean isNew() {
        return true;
    }

    /**
     * 提交当前保存的属性
     */
    public void commit() {
        try {

            for (SessionStore store : stores.values()) {
                store.commit();
            }
        } catch (Exception e) {
            log.error("session commit error", e);
        }
    }

    public MonoHttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(MonoHttpServletRequest request) {
        this.request = request;
    }

    public MonoHttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(MonoHttpServletResponse response) {
        this.response = response;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * 初始化session
     */
    public void init() {

        initStores();
        fetchSessionId();
        notifyStoresSessionReady();
    }

    private void notifyStoresSessionReady() {
        // 只初始化用到的store
        for (SessionStore store : this.stores.values()) {
            store.onSessionReady();
        }
    }

    private void fetchSessionId() {
        // 先尝试从cookie中获取
        sessionId = (String) getAttribute(SESSION_ID);

        // 如果cookie中没有，则生成新的sessionId，并写入cookie中
        if (StringUtils.isBlank(sessionId)) {
            sessionId = generateSessionId();
            setAttribute(SESSION_ID, sessionId);
        }
    }

    private String generateSessionId() {
        return DigestUtils.md5Hex(UniqID.getInstance().getUniqID());
    }

    /**
     * 初始化stores
     */
    private void initStores() {
        for (SessionStore store : stores.values()) {
            store.init();
            store.init(this);
        }

    }

    private interface Callback {

        Object doCallback(SessionStore store, AttributeConfigDO ac);
    }

    @Override
    public void destroy() {

    }

}
