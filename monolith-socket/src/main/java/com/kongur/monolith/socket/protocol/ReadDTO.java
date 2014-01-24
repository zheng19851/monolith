package com.kongur.monolith.socket.protocol;

import java.io.Serializable;

/**
 * 
 * @author zhengwei
 *
 */
public class ReadDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2735752829579482848L;
    
    
    private boolean                   success;
    //
    // /**
    // * 字符串读取所在位置
    // */
    // private int currIndex;
    //
    /**
     * 读取的字节数
     */
    private int               readBytes;

    public boolean isSuccess() {
        return success;
    }

    public ReadDTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getReadBytes() {
        return readBytes;
    }

    public ReadDTO setReadBytes(int readBytes) {
        this.readBytes = readBytes;
        return this;
    }

}
