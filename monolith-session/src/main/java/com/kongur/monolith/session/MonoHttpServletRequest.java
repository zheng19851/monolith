package com.kongur.monolith.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * 
 * mono request ½ö½öÖØÐ´getSession
 * 
 * @author zhengwei
 * @date£º2011-6-15
 */

public class MonoHttpServletRequest extends HttpServletRequestWrapper {

    private MonoHttpSession monoHttpSession;

    public MonoHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    public HttpSession getSession() {
        return this.monoHttpSession;
    }

    public void setSession(MonoHttpSession monoHttpSession) {
        this.monoHttpSession = monoHttpSession;
    }

}
