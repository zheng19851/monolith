package com.kongur.monolith.socket.message.codec;

import com.kongur.monolith.common.result.Result;

/**
 * ½âÂë½á¹û
 * 
 * @author zhengwei
 */
public class DecodeResult<USO> extends Result {

    /**
     * 
     */
    private static final long serialVersionUID = -7592745639752908823L;

    private USO               uso;

    public USO getUso() {
        return uso;
    }

    public void setUso(USO uso) {
        this.uso = uso;
    }

}
