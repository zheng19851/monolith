package com.kongur.monolith.socket.message.codec;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.socket.message.UpstreamMessage;

/**
 * ½âÂë½á¹û
 * 
 * @author zhengwei
 */
public class DecodeResult<UM extends UpstreamMessage> extends Result {

    /**
     * 
     */
    private static final long serialVersionUID = -7592745639752908823L;

    private UM                um;

    public UM getUm() {
        return um;
    }

    public void setUm(UM um) {
        this.um = um;
    }

}
