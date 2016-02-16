package com.runssnail.monolith.session;

import com.runssnail.monolith.session.attibute.AttributesConfigManager;
import com.runssnail.monolith.session.attibute.DefaultAttributesConfigManager;
import com.runssnail.monolith.session.store.CookieSessionStore;
import com.runssnail.monolith.session.store.DefaultSessionStoreFactory;
import com.runssnail.monolith.session.store.SessionStore;
import com.runssnail.monolith.session.store.SessionStoreFactory;
import com.runssnail.monolith.session.utils.ClassUtils;
import com.runssnail.monolith.session.utils.ConfigUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * monolith session filter
 *
 * @author wade.zheng
 * @date：2011-6-15
 */
public class MonoHttpSessionFilter implements Filter {

    private static final Log log = LogFactory.getLog(MonoHttpSessionFilter.class);

    /**
     * session 属性管理器
     */
    private AttributesConfigManager attributesConfigManager;

    private SessionStoreFactory sessionStoreFactory;

    private static final String ATTRIBUTES_CONFIG_MANAGER_CLASS = "attributesConfigManager";

    private static final Class<? extends SessionStore>[] DEFAULT_STORE_CLASSES = new Class[] { CookieSessionStore.class};

    private static final String ADDITIONAL_STORE_CLASSES = "additionalStoreClasses";

    private static final String STORE_POSTFIX = "Store";

    private static final char STANDARD_NAME_SEPARATOR = '-';

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;

        initAttributesConfigManager();

        initSessionStoreFactory();
    }

    private void initSessionStoreFactory() {
        Map<String, Class<? extends SessionStore>> storeTypeMap = new HashMap<String, Class<? extends SessionStore>>();
        addDefaultStoreClassesToStoreTypeMap(storeTypeMap);
        addAdditionalStoreClassesToStoreTypeMap(storeTypeMap);

        DefaultSessionStoreFactory sessionStoreFactory = new DefaultSessionStoreFactory();
        sessionStoreFactory.setStoreTypeMap(storeTypeMap);
        this.sessionStoreFactory = sessionStoreFactory;
    }

    private void addAdditionalStoreClassesToStoreTypeMap(Map<String, Class<? extends SessionStore>> storeTypeMap) {
        String param = filterConfig.getInitParameter(ADDITIONAL_STORE_CLASSES);
        String[] additionalStoreClassNames = ConfigUtils.splitConfig(param);
        if (!ArrayUtils.isEmpty(additionalStoreClassNames)) {
            for (String className : additionalStoreClassNames) {
                Class<? extends SessionStore> storeClass = ClassUtils.findClass(className, SessionStore.class);
                addToStoreTypeMap(storeClass, storeTypeMap);
            }
        }
    }

    private void addDefaultStoreClassesToStoreTypeMap(Map<String, Class<? extends SessionStore>> storeTypeMap) {
        for (Class<? extends SessionStore> storeClass : DEFAULT_STORE_CLASSES) {
            addToStoreTypeMap(storeClass, storeTypeMap);
        }
    }

    private void addToStoreTypeMap(Class<? extends SessionStore> storeClass,
                                   Map<String, Class<? extends SessionStore>> storeTypeMap) {
        String className = storeClass.getSimpleName();
        String standardName = convertClassNameToStandardName(className, STORE_POSTFIX);
        storeTypeMap.put(standardName, storeClass);
    }

    /**
     * 将形式如"AbcDefStore"的类名(假设postfix为"Store")，转换为形式如"abc-def"的标准名称
     */
    private String convertClassNameToStandardName(String className, String postfix) {
        if (StringUtils.endsWith(className, postfix)) {
            className = StringUtils.substringBeforeLast(className, postfix);
        }
        String[] words = StringUtils.splitByCharacterTypeCamelCase(className);
        String joinedWords = StringUtils.join(words, STANDARD_NAME_SEPARATOR);
        String standardName = StringUtils.lowerCase(joinedWords);
        return standardName;
    }

    private void initAttributesConfigManager() throws ServletException {
        if (filterConfig == null) {
            return;
        }

        // 设置attributesConfigManager
        String attributesConfigManagerClazz = filterConfig.getInitParameter(ATTRIBUTES_CONFIG_MANAGER_CLASS);

        if (StringUtils.isNotBlank(attributesConfigManagerClazz)) {
            try {
                Class clazz = ClassUtils.findClass(attributesConfigManagerClazz, AttributesConfigManager.class);
                attributesConfigManager = (AttributesConfigManager) clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        } finally {

            session.commit(); // 将修改过的cookie添加到response里

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
        Map<String, SessionStore> stores = this.sessionStoreFactory.createStores();
        MonoHttpSession session = new MonoHttpSession(monoRequest, monoResponse, this.filterConfig.getServletContext(), stores,
                attributesConfigManager);
        session.init();
        return session;
    }

    @Override
    public void destroy() {
        this.attributesConfigManager.destroy();
    }

    public void setAttributesConfigManager(AttributesConfigManager attributesConfigManager) {
        this.attributesConfigManager = attributesConfigManager;
    }

}
