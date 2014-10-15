package com.runssnail.monolith.socket.mina;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

import com.runssnail.monolith.socket.message.codec.AbstractFixedMessageDecoder;
import com.runssnail.monolith.socket.message.codec.CodecException;
import com.runssnail.monolith.socket.message.codec.CodecUtils;
import com.runssnail.monolith.socket.message.codec.DecodeResult;
import com.runssnail.monolith.socket.message.header.UpstreamHeader;

/**
 * ��������������ת������ 6200
 * 
 * @author zhengwei
 */
// @Service("6200MessageDecoder")
public class EBankToExchangeMessageDecoder extends AbstractFixedMessageDecoder<EBankToExchangeRequest> {

    @Override
    protected void doDecode(EBankToExchangeRequest uso, ByteBuffer buffer, CharsetDecoder decoder,
                                    DecodeResult<EBankToExchangeRequest> result) throws CodecException {
        // �����˺� C(30) ���пͻ������˺� M M
        // �����˺� C(30) �������Ľ����˺� M M
        // ���ִ��� C(4) CNY������� HKD���۱� USD����Ԫ M M
        // ת�˽�� N(15) ��λ��ȷ���� M M
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