package com.kongur.monolith.session;

import javax.servlet.http.Cookie;

/**
 * mono cookie
 * 
 * @author zhengwei
 * @date：2011-6-15
 */

public class MonoCookie extends Cookie {

    /**
     * 是否只能是https读取
     */
    private boolean httpOnly;

    public MonoCookie(String name, String value) {
        super(name, value);
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

}
