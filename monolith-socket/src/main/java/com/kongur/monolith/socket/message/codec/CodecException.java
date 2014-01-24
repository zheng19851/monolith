package com.kongur.monolith.socket.message.codec;


/**
 * 编解码器异常，当解析和校验报文时会出这个错误
 * 
 * @author zhengwei
 */
public class CodecException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -8435492245815336489L;

    private String            transCode;

    private String            errorCode;

    private String            errorMsg;

    public CodecException(Throwable cause) {
        super(cause);
    }

    public CodecException(String errorCode, String errorMsg) {
        this(null, errorCode, errorMsg);
    }

    public CodecException(String transCode, String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.transCode = transCode;
    }

    public CodecException(String m) {
        super(m);
    }

    public CodecException(EnumMessageErrorCode error) {
        this(error.getCode(), error.getMsg());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public CodecException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public CodecException setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getTransCode() {
        return transCode;
    }

    public CodecException setTransCode(String transCode) {
        this.transCode = transCode;
        return this;
    }

}
