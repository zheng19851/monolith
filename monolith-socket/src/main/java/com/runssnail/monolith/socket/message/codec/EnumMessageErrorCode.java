package com.runssnail.monolith.socket.message.codec;

/**
 * 报文解析错误代码
 * 
 * @author zhengwei
 */
public enum EnumMessageErrorCode {
    
    SUCCESS("0000", "成功"),
    ERROR("9999", "服务器出错"),
    
    
    MESSAGE_ERROR("4100", "报文错误"),
    
    HEADER_LEN_ERROR("4101", "报文头长度错误"),
    BODY_LEN_ERROR("4102", "报文体长度错误"),
    TRANS_CODE_ERROR("4103", "交易代码错误"), 
    PARAMS_ERROR("4104", "参数错误"),
    
    
    
    ;

    private EnumMessageErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static boolean isSuccess(String errorCode) {
        return SUCCESS.getCode().equals(errorCode);
    }

    public static boolean isSysError(String v) {
        return ERROR.getCode().equals(v);
    }

}
