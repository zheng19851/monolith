package com.kongur.monolith.socket.message;

import org.apache.log4j.Logger;

import com.kongur.monolith.common.DomainBase;
import com.kongur.monolith.socket.message.header.DownstreamHeader;

/**
 * @author zhengwei
 */
public abstract class AbstractDownstreamMessage extends DomainBase implements DownstreamMessage {

    /**
     * 
     */
    private static final long serialVersionUID = 791276338130790653L;

    protected final Logger    log              = Logger.getLogger(getClass());

    /**
     * 头部数据
     */
    private DownstreamHeader  header;

    public AbstractDownstreamMessage(DownstreamHeader header) {
        this.header = header;
    }

    @Override
    public String getTransCode() {
        return header.getTransCode();
    }

    @Override
    public boolean isSuccess() {
        return header.isSuccess();
    }

    @Override
    public DownstreamHeader getDownstreamHeader() {
        return header;
    }

    public void setTransCode(String transCode) {
        this.header.setTransCode(transCode);
    }

}
