package com.kongur.monolith.weixin.core.domain.message;

/**
 * 微信平台api出错信息
 * 
 * @author zhengwei
 * @date 2014-2-18
 */
public enum WeixinApiErrorCode {

    ACCESS_TOKEN_ERROR("40001", "获取access_token时AppSecret错误，或者access_token无效 ")

    ;

    private String errorCode;

    private String errorInfo;

    private WeixinApiErrorCode(String errorCode, String errorInfo) {
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    /**
     * 是否AccessToken错误
     * 
     * @param errcode
     * @return
     */
    public static boolean isAccessTokenError(String errcode) {
        return ACCESS_TOKEN_ERROR.getErrorCode().equalsIgnoreCase(errcode);
    }

}
