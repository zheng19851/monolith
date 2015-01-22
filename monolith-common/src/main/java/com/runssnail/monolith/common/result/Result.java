package com.runssnail.monolith.common.result;

import com.runssnail.monolith.common.DomainBase;

/**
 * 通用结果
 * 
 * @author zhengwei
 */
public class Result<T> extends DomainBase {

    /**
     * 
     */
    private static final long serialVersionUID = -9075544910442561812L;

    private boolean           success          = false;

    private String            resultCode;

    private String            resultInfo;

    /**
     * 返回结果
     */
    private T                 result;

    public Result() {

    }

    public Result(boolean success, String resultCode, String resultInfo) {
        this.success = success;
        this.resultCode = resultCode;
        this.resultInfo = resultInfo;
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result<T> setError(String resultCode, String resultInfo) {
        this.resultCode = resultCode;
        this.resultInfo = resultInfo;
        this.success = false;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public Result<T> setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public Result<T> setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
        return this;
    }

    public T getResult() {
        return result;
    }

    public Result<T> setResult(T result) {
        this.result = result;
        return this;
    }

}
