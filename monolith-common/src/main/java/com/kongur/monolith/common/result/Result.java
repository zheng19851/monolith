package com.kongur.monolith.common.result;

import java.io.Serializable;

/**
 * 通用结果
 * 
 * @author zhengwei
 */
public class Result<T> implements Serializable {

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

    public void setError(String resultCode, String resultInfo) {
        this.resultCode = resultCode;
        this.resultInfo = resultInfo;
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
