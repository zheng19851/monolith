package com.kongur.monolith.socket.mina;


import com.kongur.monolith.socket.message.codec.EnumMessageErrorCode;

/**
 * œÏ”¶Õ∑
 * 
 * @author zhengwei
 */
public abstract class ResponseHeader extends Header {

    public ResponseHeader() {
    }

    public ResponseHeader(String transCode) {
        super(transCode);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1105167186990575637L;

    private String            errorCode        = EnumMessageErrorCode.SUCCESS.getCode();

    private String            errorMsg         = EnumMessageErrorCode.SUCCESS.getMsg();

    // @Override
    // public void encode(IoBuffer header, CharsetEncoder encoder) throws CharacterCodingException {
    // // ignore
    // }
    //
    // @Override
    // public void decode(IoBuffer header, CharsetDecoder decoder) throws CharacterCodingException {
    // // ignore
    //
    // }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean isSuccess() {
        return EnumMessageErrorCode.isSuccess(this.errorCode);
    }

    @Override
    public boolean isSysError() {
        return EnumMessageErrorCode.isSysError(this.errorCode);
    }

    @Override
    public boolean isRequest() {
        return false;
    }

}
