package com.kongur.monolith.im.domain;

import com.kongur.monolith.common.result.Result;

/**
 * 调用服务 返回的结果
 * 
 * @author zhengwei
 * @date 2014-2-17
 */
public class ServiceResult<T> extends Result {

    /**
     * 
     */
    private static final long serialVersionUID = 5708921597192889845L;

    private T                 result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
