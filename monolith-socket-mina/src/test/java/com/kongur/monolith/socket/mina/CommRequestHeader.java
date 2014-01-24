package com.kongur.monolith.socket.mina;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.message.codec.CodecUtils;
import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * 交易前置用的请求头
 * 
 * 交易所发起
 * 
 * @author zhengwei
 */
public class CommRequestHeader extends RequestHeader implements UpstreamHeader {

    public CommRequestHeader(String transCode) {
        super(transCode);
    }

    public CommRequestHeader() {

    }

    /**
     * 
     */
    private static final long serialVersionUID = -3225390310291177888L;

    /**
     * 交易所代码
     */
    private String            exchangeNo;

    /**
     * 银行代码
     */
    private String            bankNo;

    public String getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(String exchangeNo) {
        this.exchangeNo = exchangeNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    @Override
    public void decode(java.nio.ByteBuffer buffer, CharsetDecoder decoder) throws CharacterCodingException {
        // 版本号 2 目前默认01
        // 交易代码 4 0001
        // 交易所编号 6
        // 银行代码 6
        // 请求方流水号 20 不足补空
        // 交易日期 8 YYYYMMDD
        // 交易时间 6 HH24MiSS
        // 记录条数 8 前补0

        this.setVersion(CodecUtils.getString(buffer, 0, 2, decoder));
        this.setTransCode(CodecUtils.getString(buffer, 2, 4, decoder));
        this.setExchangeNo(CodecUtils.getString(buffer, 6, 6, decoder));
        this.setBankNo(CodecUtils.getString(buffer, 12, 6, decoder));
        this.setBizNo(CodecUtils.getString(buffer, 18, 20, decoder));
        this.setTransDate(CodecUtils.getString(buffer, 38, 8, decoder));
        this.setTransTime(CodecUtils.getString(buffer, 46, 6, decoder));
        this.setFileCount(CodecUtils.getInt(buffer, 52, 8, decoder));
    }

    @Override
    public int getBytesLen() {
        return Constants.COMM_REQUEST_HEADER_BYTES_LEN;
    }

}
