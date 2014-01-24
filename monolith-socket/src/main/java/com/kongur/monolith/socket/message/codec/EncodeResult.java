package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;

import com.kongur.monolith.common.result.Result;

/**
 * @author zhengwei
 */
public class EncodeResult extends Result {

    /**
     * 
     */
    private static final long serialVersionUID = 5757692437430282356L;

    /**
     * 定长部分buff
     */
    private ByteBuffer          buffer;

    /**
     * 多条记录时设置的buff
     */
    private ByteBuffer          multiBuff;

    public ByteBuffer getMultiBuff() {
        return multiBuff;
    }

    public void setMultiBuff(ByteBuffer multiBuff) {
        this.multiBuff = multiBuff;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

}
