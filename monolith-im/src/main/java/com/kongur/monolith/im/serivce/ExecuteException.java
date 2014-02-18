package com.kongur.monolith.im.serivce;

/**
 * @author zhengwei
 * @date 2014-2-17
 */
public class ExecuteException extends RuntimeException {

    public ExecuteException(String msg, Exception e) {
        super(msg, e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 210878875098375288L;

}
