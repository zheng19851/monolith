package com.kongur.monolith.socket.mina;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.util.Date;

import com.kongur.monolith.lang.DateUtil;
import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.socket.message.codec.CodecException;
import com.kongur.monolith.socket.message.codec.CodecUtils;
import com.kongur.monolith.socket.message.header.DownstreamHeader;

/**
 * 跟交易前置交互的响应报文头, 交易所发起 银商系统响应头
 * 
 * @author zhengwei
 */
public class CommResponseHeader extends ResponseHeader implements DownstreamHeader {

    public CommResponseHeader(String transCode) {
        super(transCode);
    }

    public CommResponseHeader() {
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7567803945784009881L;

    /**
     * 响应方流水号
     */
    private String            resBizNo;

    public String getResBizNo() {
        return resBizNo;
    }

    public void setResBizNo(String resBizNo) {
        this.resBizNo = resBizNo;
    }

    @Override
    public ByteBuffer encode(CharsetEncoder encoder) throws CodecException {

        // 交易代码 4 功能号
        // 请求方流水号 20 不足补空
        // 应答方流水号 20 不足补空
        // 主机日期 8 YYYYMMDD
        // 记录条数 8 前补0
        // 返回码 4 0000-正常，其他均为失败
        // 返回信息 60 具体错误信息

        ByteBuffer header = ByteBuffer.allocate(getBytesLen());
        header.put(CodecUtils.getBufferAlignLeft(this.getTransCode(), 4, encoder));
        header.put(CodecUtils.getBufferAlignLeft(this.getBizNo(), 20, encoder));
        header.put(CodecUtils.getBufferAlignLeft(this.getResBizNo(), 20, encoder));

        String transDate = this.getTransDate();
        if (StringUtil.isBlank(transDate)) {
            transDate = DateUtil.getDateTime(com.kongur.monolith.socket.Constants.DEFAULT_DATE_FORMAT_STR, new Date());
        }
        header.put(CodecUtils.getBufferAlignLeft(transDate, 8, encoder));
        header.put(CodecUtils.getIntBuffer(this.getFileCount(), 8, encoder));
        header.put(CodecUtils.getBufferAlignLeft(this.getErrorCode(), 4, encoder));
        header.put(CodecUtils.getBufferAlignLeft(this.getErrorMsg(), 60, encoder));
        header.flip();
        
        
        return header;

    }

    @Override
    public int getBytesLen() {
        return Constants.COMM_RESPONSE_HEADER_BYTES_LEN;
    }

}
