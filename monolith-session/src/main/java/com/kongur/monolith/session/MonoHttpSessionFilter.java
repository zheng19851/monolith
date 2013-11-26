package com.kongur.monolith.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kongur.monolith.session.attibute.AttributesConfigManager;
import com.kongur.monolith.session.attibute.DefaultAttributesConfigManager;
import com.kongur.monolith.session.store.CookieSessionAttributeStore;
import com.kongur.monolith.session.store.SessionAttributeStore;

/**
 * monolith session filter
 * 
 * @author wade.zheng
 * @date：2011-6-15
 */

public class MonoHttpSessionFilter implements Filter {

    private static final Logger         log                             = Logger.getLogger(MonoHttpSessionFilter.class);

    /**
     * session 属性管理器
     */
    private AttributesConfigManager     attributesConfigManager;

    /**
     * SessionAttributeStore
     */
    private List<SessionAttributeStore> stores;

    private static final String         ATTRIBUTES_CONFIG_MANAGER_CLASS = "attributesConfigManager";

    private static final String         SESSION_STORES                  = "sessionStores";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        loadAttributesConfigManager(filterConfig);

        loadSessionStores(filterConfig);
    }

    private void loadSessionStores(FilterConfig filterConfig) throws ServletException {
        if (filterConfig == null) {
            return;
        }

        // 设置sessionStores
        String sessionStores = filterConfig.getInitParameter(SESSION_STORES);
        if (StringUtils.isNotBlank(sessionStores)) {
            String[] sessionStoresArr = null;
            if (sessionStores.contains(",")) {
                sessionStoresArr = sessionStores.split(",");
            } else {
                sessionStoresArr = new String[] { sessionStores };
            }

            if (stores == null) {
                this.stores = new ArrayList<SessionAttributeStore>(sessionStoresArr.length);
            }

            for (String storeClazz : sessionStoresArr) {
                try {
                    Class clazz = ClassUtils.getClass(storeClazz);
                    SessionAttributeStore store = (SessionAttributeStore) clazz.newInstance();
                    if (this.stores.contains(store)) {
                        throw new RuntimeException("duplicate SessionAttributeStore, class of (" + storeClazz + ")");
                    }
                    this.stores.add(store);
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }

        }

        if (this.stores == null) {
            this.stores = new ArrayList<SessionAttributeStore>(1);
            this.stores.add(new CookieSessionAttributeStore());
        }

    }

    private void loadAttributesConfigManager(FilterConfig filterConfig) throws ServletException {
        if (filterConfig == null) {
            return;
        }

        // 设置attributesConfigManager
        String attributesConfigManagerClazz = filterConfig.getInitParameter(ATTRIBUTES_CONFIG_MANAGER_CLASS);

        if (StringUtils.isNotBlank(attributesConfigManagerClazz)) {
            try {
                Class clazz = ClassUtils.getClass(attributesConfigManagerClazz);
                attributesConfigManager = (AttributesConfigManager) clazz.newInstance();
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        if (attributesConfigManager == null) {
            DefaultAttributesConfigManager defaultAttributesConfigManager = new DefaultAttributesConfigManager();
            attributesConfigManager = defaultAttributesConfigManager;
        }

        attributesConfigManager.init();

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                             ServletException {
        // 对于重入的filter，不消化exception。
        // 在weblogic中，servlet forward到jsp时，jsp仍会调用此filter，而jsp抛出的异常就会被该filter捕获。
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)
            || (request.getAttribute(getClass().getName()) != null)) {
            chain.doFilter(request, response);
            return;
        }

        // 防止重入.
        request.setAttribute(getClass().getName(), Boolean.TRUE);

        HttpServletRequest httpReq = (HttpServletRequest) request;
        MonoHttpServletRequest monoRequest = new MonoHttpServletRequest(httpReq);
        MonoHttpServletResponse monoResponse = new MonoHttpServletResponse((HttpServletResponse) response);

        MonoHttpSession session = createMonoHttpSession(monoRequest, monoResponse, httpReq.getSession());
        monoRequest.setSession(session);
        monoResponse.setSession(session);

        try {
            chain.doFilter(monoRequest, monoResponse);
            if (session != null) {
                session.commit(); // 将修改过的cookie添加到response里
            }
        } finally {

            // 将缓存数据写进response流里
            monoResponse.commitBuffer();
        }

    }

    /**
     * 创建session
     * 
     * @param monoRequest
     * @param monoResponse
     * @param httpSession
     * @return
     */
    private MonoHttpSession createMonoHttpSession(MonoHttpServletRequest monoRequest,
                                                  MonoHttpServletResponse monoResponse, HttpSession httpSession) {
        List<SessionAttributeStore> stores = getStores();
        MonoHttpSession session = new MonoHttpSession(monoRequest, monoResponse, httpSession, stores,
                                                      attributesConfigManager);
        session.init();
        return session;
    }

    /**
     * 获取SessionStores
     * 
     * @return
     */
    private List<SessionAttributeStore> getStores() {

        if (this.stores != null) {
            return this.stores;
        }

        List<SessionAttributeStore> stores = new ArrayList<SessionAttributeStore>(1);
        CookieSessionAttributeStore cookieStore = new CookieSessionAttributeStore();
        stores.add(cookieStore);
        return stores;
    }

    @Override
    public void destroy() {
        this.attributesConfigManager.destroy();
        if (this.stores != null && !this.stores.isEmpty()) {
            for (SessionAttributeStore store : this.stores) {
                store.destroy();
            }
        }
    }

    public void setAttributesConfigManager(AttributesConfigManager attributesConfigManager) {
        this.attributesConfigManager = attributesConfigManager;
    }

}
