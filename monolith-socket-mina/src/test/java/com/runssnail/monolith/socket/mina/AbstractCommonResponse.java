package com.runssnail.monolith.socket.mina;


import com.runssnail.monolith.socket.message.AbstractDownstreamMessage;
import com.runssnail.monolith.socket.message.DownstreamMessage;

/**
 * 返回给交易前置的响应报文
 * 
 * @author zhengwei
 */
public class AbstractCommonResponse extends AbstractDownstreamMessage implements DownstreamMessage, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 7063714172329449434L;

    public AbstractCommonResponse(String transCode) {
        super(new CommResponseHeader(transCode));
    }

    public AbstractCommonResponse(CommResponseHeader responseHeader) {
        super(responseHeader);
    }

    public String getResBizNo() {
        return getCommResponseHeader().getResBizNo();
    }

    public void setResBizNo(String resBizNo) {
        getCommResponseHeader().setResBizNo(resBizNo);
    }

    public CommResponseHeader getCommResponseHeader() {
        return (CommResponseHeader) getDownstreamHeader();
    }

    public String getErrorCode() {
        return getCommResponseHeader().getErrorCode();
    }

    public void setErrorCode(String errorCode) {
        getCommResponseHeader().setErrorCode(errorCode);
    }

    public String getErrorMsg() {
        return getCommResponseHeader().getErrorMsg();
    }

    public void setErrorMsg(String errorMsg) {
        getCommResponseHeader().setErrorMsg(errorMsg);
    }

    @Override
    public String getBizNo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBizNo(String bizNo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getTransDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTransDate(String transDate) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFileName(String fileName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getFileCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFileCount(int fileCount) {
        // TODO Auto-generated method stub
        
    }

}
