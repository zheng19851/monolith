package com.runssnail.monolith.socket.mina;

/**
 * 报文响应
 * 
 * @author zhengwei
 */
public interface Response {

    /**
     * 交易号
     * 
     * @return
     */
    String getTransCode();

    public String getBizNo();

    public void setBizNo(String bizNo);

    public String getTransDate();

    public void setTransDate(String transDate);

    public String getFileName();

    public void setFileName(String fileName);

    public int getFileCount();

    public void setFileCount(int fileCount);

    public String getErrorCode();

    public void setErrorCode(String errorCode);

    public String getErrorMsg();

    public void setErrorMsg(String errorMsg);
    
    /**
     * 响应数据是否正确，根据errorCode判断
     * 
     * @return
     */
    boolean isSuccess();

    // String getResBizNo();

}
