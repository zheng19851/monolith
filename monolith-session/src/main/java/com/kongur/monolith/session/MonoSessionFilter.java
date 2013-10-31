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

import com.kongur.monolith.session.attibute.AttributesConfigManager;
import com.kongur.monolith.session.attibute.DefaultAttributesConfigManager;

/**
 * monolith session filter
 * 
 * @author zhengwei
 * @date：2011-6-15
 */

public class MonoSessionFilter implements Filter {

    private FilterConfig            filterConfig;

    private AttributesConfigManager attributesConfigManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        initAttributesConfigManager();
    }

    /**
     * 初始化AttributesConfigManager
     */
    private void initAttributesConfigManager() {
        attributesConfigManager = getAttributesConfigManager();
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

        MonoHttpServletRequest monoRequest = null;
        MonoHttpServletResponse monoResponse = null;
        MonoHttpSession session = null;
        monoRequest = new MonoHttpServletRequest((HttpServletRequest) request);
        monoResponse = new MonoHttpServletResponse((HttpServletResponse) response);
        session = createMonoHttpSession(monoRequest, monoResponse);
        monoRequest.setSession(session);
        monoResponse.setSession(session);

        try {
            chain.doFilter(monoRequest, monoResponse);
            if (session != null) {
                session.commit(); // 默认是先将数据写进缓存
            }
        } finally {
            
            // 真正的写cookie到流里
            monoResponse.commitBuffer();
        }
    }

    private MonoHttpSession createMonoHttpSession(MonoHttpServletRequest simpleRequest,
                                              MonoHttpServletResponse simpleResponse) {
        List<SessionAttributeStore> stores = getStores();
        MonoHttpSession session = new MonoHttpSession(simpleRequest, simpleResponse, filterConfig.getServletContext(),
                                                  stores, attributesConfigManager);
        session.init();
        return session;
    }

    /**
     * 获取AttributesConfigManager
     * 
     * @return
     */
    private AttributesConfigManager getAttributesConfigManager() {
        DefaultAttributesConfigManager attributesConfigManager = new DefaultAttributesConfigManager();
        attributesConfigManager.init();
        return attributesConfigManager;
    }

    /**
     * 获取SessionStores
     * 
     * @return
     */
    private List<SessionAttributeStore> getStores() {
        List<SessionAttributeStore> stores = new ArrayList<SessionAttributeStore>();

        stores.add(new CookieSessionAttributeStore());
        return stores;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
