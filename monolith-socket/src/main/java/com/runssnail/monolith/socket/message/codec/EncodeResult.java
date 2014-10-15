package com.runssnail.monolith.socket.message.codec;

import java.nio.ByteBuffer;

import com.runssnail.monolith.common.result.Result;

/**
 * 编码结果
 * 
 * @author zhengwei
 */
public class EncodeResult extends Result {

    /**
     * 
     */
    private static final long serialVersionUID = 5757692437430282356L;

    /**
     * 编码出来的ByteBuffer
     */
    private ByteBuffer        buffer;

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

}
