package com.runssnail.monolith.socket.mina;


/**
 * 跟交易前置交互的报文头
 * 
 * @author zhengwei
 */
public abstract class RequestHeader extends Header {

    public RequestHeader(String transCode) {
        super(transCode);
    }

    public RequestHeader() {
    }

    /**
     * 
     */
    private static final long serialVersionUID = 5177218827191667352L;

    /**
     * 交易所机构
     */
    private String            instNo;

    /**
     * 核算机构
     */
    private String            transInstNo;

    /**
     * 柜员号
     */
    private String            virTellerNo;

    /**
     * 时间
     */
    private String            transTime;

    public String getInstNo() {
        return instNo;
    }

    public void setInstNo(String instNo) {
        this.instNo = instNo;
    }

    public String getTransInstNo() {
        return transInstNo;
    }

    public void setTransInstNo(String transInstNo) {
        this.transInstNo = transInstNo;
    }

    public String getVirTellerNo() {
        return virTellerNo;
    }

    public void setVirTellerNo(String virTellerNo) {
        this.virTellerNo = virTellerNo;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

//    @Override
//    public void encode(IoBuffer header, CharsetEncoder encoder) throws CharacterCodingException {
//        // ignore
//    }
//
//    @Override
//    public void decode(IoBuffer headerBuffer, CharsetDecoder decoder) throws CharacterCodingException {
//        // ignore
//
//    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isSysError() {
        return false;
    }

}
