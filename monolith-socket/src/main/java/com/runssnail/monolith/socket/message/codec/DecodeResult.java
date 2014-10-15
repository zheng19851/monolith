package com.runssnail.monolith.socket.message.codec;

import com.runssnail.monolith.common.result.Result;
import com.runssnail.monolith.socket.message.UpstreamMessage;

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

    private UM                upsteamMessage;

    public UM getUpsteamMessage() {
        return upsteamMessage;
    }

    public void setUpsteamMessage(UM upsteamMessage) {
        this.upsteamMessage = upsteamMessage;
    }

}
