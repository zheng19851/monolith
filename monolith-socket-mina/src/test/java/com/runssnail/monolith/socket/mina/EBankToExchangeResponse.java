package com.runssnail.monolith.socket.mina;


/**
 * 6200 交易所发起的， 银行转交易所
 * 
 * 
 * @author zhengwei
 *
 */
public class EBankToExchangeResponse extends TransferResponse {

	/**
	 * bankAccount 银行账号 M
	 * fundAccount 交易账号 M
	 * moneyType   币种代码 M
	 * transAmount 转账金额 M
	 */
    public EBankToExchangeResponse() {
        super("6200");
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1438042273977910765L;

}
