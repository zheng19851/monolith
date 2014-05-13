package com.kongur.monolith.weixin.core.service;

/**
 * 调用平台服务抛的异常
 * 
 * @author zhengwei
 * @date 2014-2-17
 */
public class ApiException extends RuntimeException {

    public ApiException() {
    }

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(Exception e) {
        super(e);
    }

    public ApiException(String msg, Exception e) {
        super(msg, e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 210878875098375288L;

}
