package com.runssnail.monolith.socket.mina;

import java.io.Serializable;

import com.runssnail.monolith.common.DomainBase;

/**
 * 请求头
 * 
 * @author zhengwei
 */
public abstract class Header extends DomainBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7678671306504236142L;

    /**
     * 版本号
     */
    private String            version          = "01";

    /**
     * 交易代码
     */
    private String            transCode;

    /**
     * 流水号
     */
    private String            bizNo;

    /**
     * 日期
     */
    private String            transDate;

    /**
     * 后补空，是随机一个数字
     */
    private String            fileName;

    /**
     * 记录条数，前补0
     */
    private int               fileCount        = 0;

    public Header() {
    }

    public Header(String transCode) {
        this.transCode = transCode;
    }

//    /**
//     * 响应和发送请求时编码报文头
//     * 
//     * @param header
//     * @param encoder
//     * @throws CharacterCodingException
//     */
//    public abstract void encode(IoBuffer header, CharsetEncoder encoder) throws CharacterCodingException;
//
//    /**
//     * 解码, 接收到请求或者收到响应时，需要实现这个方法
//     * 
//     * @param header
//     * @param decoder
//     * @throws CharacterCodingException
//     */
//    public abstract void decode(IoBuffer header, CharsetDecoder decoder) throws CharacterCodingException;

    /**
     * 头部字节长度
     * 
     * @return
     */
    public abstract int getBytesLen();
    
    /**
     * 是否有系统错误
     * 
     * @return
     */
    public abstract boolean isSysError();

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isSuccess() {
        return true;
    }

    public abstract boolean isRequest();

    /**
     * 是否是有多条记录情况的协议响应报文
     * 
     * @return
     */
    public boolean isMultResponse() {
        return false;
    }
}
