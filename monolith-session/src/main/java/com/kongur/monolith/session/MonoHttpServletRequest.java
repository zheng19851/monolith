package com.kongur.monolith.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * mono request
 * 
 * @author zhengwei
 * @date£º2011-6-15
 */

public class MonoHttpServletRequest extends HttpServletRequestWrapper {

    private MonoHttpSession simpleSession;

    public MonoHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    public HttpSession getSession() {
        return this.simpleSession;
    }

    public void setSession(MonoHttpSession simpleSession) {
        this.simpleSession = simpleSession;
    }

}
