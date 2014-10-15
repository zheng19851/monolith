package com.runssnail.monolith.socket.mina;

import com.runssnail.monolith.socket.message.AbstractUpstreamMessage;
import com.runssnail.monolith.socket.message.UpstreamMessage;
import com.runssnail.monolith.socket.message.header.UpstreamHeader;

/**
 * 交易前置交互的请求基类(交易所请求银商系统)
 * 
 * @author zhengwei
 */
public abstract class AbstractCommonReq extends AbstractUpstreamMessage implements UpstreamMessage, Request {

    public AbstractCommonReq(String transCode) {
        super(new CommRequestHeader(transCode));
    }

    public AbstractCommonReq(UpstreamHeader header) {
        super(header);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 473700156238812736L;

    public CommRequestHeader getCommRequestHeader() {
    	return (CommRequestHeader) this.getHeader();
    }
    
    public RequestHeader getRequestHeader() {
        return (RequestHeader) getUpstreamHeader();
    }

    public String getInstNo() {
        return getRequestHeader().getInstNo();
    }

    public void setInstNo(String instNo) {
        getRequestHeader().setInstNo(instNo);
    }
    
    public String getExchangeNo() {
    	return getCommRequestHeader().getExchangeNo();
    }
    
    public void setExchangeNo(String exchangeNo) {
    	getCommRequestHeader().setExchangeNo(exchangeNo);
    }
    

}
