package com.runssnail.monolith.socket.buffer;

import java.nio.ByteBuffer;

/**
 * 
 * ByteBuffer helper
 * 
 * @author zhengwei
 */
public class ByteBuffers {

    /**
     * 切块
     * 
     * @param src
     * @param index 起始位置
     * @param length 长度
     * @return
     */
    public static final ByteBuffer getSlice(ByteBuffer src, int index, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length: " + length);
        }

        int limit = src.limit();

        if (index > limit) {
            throw new IllegalArgumentException("index: " + index);
        }

        int endIndex = index + length;

        if (src.capacity() < endIndex) {
            throw new IndexOutOfBoundsException("index + length (" + endIndex + ") is greater " + "than capacity ("
                                                + src.capacity() + ").");
        }

        src.clear().position(index).limit(endIndex);

        ByteBuffer slice = src.slice();
        src.position(index);
        src.limit(limit);
        return slice;
    }

}
