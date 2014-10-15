package com.runssnail.monolith.socket.message.header;

/**
 * 报文头
 * 
 * @author zhengwei
 * 
 * @date 2014-1-26
 *
 */
public interface Header {
    
    /**
     * 交易代码
     * 
     * @return
     */
    public String getTransCode();

    public void setTransCode(String transCode);

    /**
     * 是否成功
     * 
     * @return
     */
    public boolean isSuccess();

    /**
     * 报文头固定长度(字节)
     * 
     * @return
     */
    public int getBytesLen();

}
