package com.kongur.monolith.socket.mina;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

import com.kongur.monolith.socket.message.codec.AbstractFixedMessageDecoder;
import com.kongur.monolith.socket.message.codec.CodecUtils;
import com.kongur.monolith.socket.message.codec.DecodeResult;
import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * 交易所发起，银行转交易所 6200
 * 
 * @author zhengwei
 */
// @Service("6200MessageDecoder")
public class EBankToExchangeMessageDecoder extends AbstractFixedMessageDecoder<EBankToExchangeRequest> {

    @Override
    protected void doDecodeFixedBuf(EBankToExchangeRequest uso, ByteBuffer buffer, CharsetDecoder decoder,
                                    DecodeResult<EBankToExchangeRequest> result) throws CharacterCodingException {
        // 银行账号 C(30) 银行客户结算账号 M M
        // 交易账号 C(30) 交易所的交易账号 M M
        // 币种代码 C(4) CNY－人民币 HKD－港币 USD－美元 M M
        // 转账金额 N(15) 单位精确到分 M M
        uso.setBankAccount(CodecUtils.getString(buffer, 0, 30, decoder));
        uso.setFundAccount(CodecUtils.getString(buffer, 30, 30, decoder));
        uso.setMoneyType(CodecUtils.getString(buffer, 60, 3, decoder));
        uso.setTransAmount(CodecUtils.getLong(buffer, 63, 15, decoder));
    }

    @Override
    public EBankToExchangeRequest createUpstreamMessage(UpstreamHeader header) {
        return new EBankToExchangeRequest();
    }

}
