package com.kongur.monolith.socket.mina;

import com.kongur.monolith.socket.message.header.UpstreamHeader;


/**
 * 
 * 交易所发起的， 银行转交易所 6200
 * 
 * @author zhengwei
 *
 */
public class EBankToExchangeRequest extends TransferRequest {

	/**
	 * bankAccount 银行账号 	(必填)
	 * fundAccount 交易账号 	(必填)
	 * moneyType   币种代码	(必填)
	 * transAmount 转账金额	(必填)
	 */
    public EBankToExchangeRequest() {
        super("6200");
    }

    public EBankToExchangeRequest(UpstreamHeader header) {
        super(header);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1629882582416731835L;

}
