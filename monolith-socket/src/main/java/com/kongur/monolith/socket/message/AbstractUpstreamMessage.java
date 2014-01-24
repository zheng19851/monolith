package com.kongur.monolith.socket.message;

import com.kongur.monolith.common.DomainBase;
import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * @author zhengwei
 */
public abstract class AbstractUpstreamMessage extends DomainBase implements UpstreamMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 4573854638741109827L;
    private UpstreamHeader    header;

    public AbstractUpstreamMessage(UpstreamHeader header) {
        this.header = header;
    }

    public UpstreamHeader getHeader() {
        return this.header;
    }

    @Override
    public void setUpstreamHeader(UpstreamHeader header) {
        this.header = header;
    }

    @Override
    public UpstreamHeader getUpstreamHeader() {
        return this.header;
    }

    @Override
    public String getTransCode() {
        return this.header.getTransCode();
    }
    
    public void setTransCode(String transCode) {
        this.header.setTransCode(transCode);
    }
    
    public boolean isSuccess() {
        return this.header.isSuccess();
    }

}
