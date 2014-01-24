package com.kongur.monolith.socket.mina;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;

import com.kongur.monolith.socket.message.codec.AbstractFixedMessageEncoder;
import com.kongur.monolith.socket.message.codec.CodecUtils;

/**
 * 交易所发起，银行转交易所 6200
 * 
 * @author zhengwei
 */
// @Service("6200MessageEncoder")
public class EBankToExchangeMessageEncoder extends AbstractFixedMessageEncoder<TransferResponse> {

    @Override
    protected ByteBuffer doEncode(TransferResponse dso, CharsetEncoder encoder) throws CharacterCodingException {

        ByteBuffer buffer = allocateBuffer();
        // 银行账号 C(30) 银行客户结算账号 M
        // 交易账号 C(30) 交易所交易账号 M
        // 货币代码 C(3) CNY－人民币 HKD－港币 USD－美元 M
        // 转账金额 N(15) 单位精确到分 M

        buffer.put(CodecUtils.getBufferAlignLeft(dso.getBankAccount(), 30, encoder));
        buffer.put(CodecUtils.getBufferAlignLeft(dso.getFundAccount(), 30, encoder));
        buffer.put(CodecUtils.getBufferAlignLeft(dso.getMoneyType(), 3, encoder));
        buffer.put(CodecUtils.getLongBuffer(dso.getTransAmount(), 15, encoder));

        buffer.flip();
        return buffer;
    }

}
