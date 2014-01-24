package com.kongur.monolith.socket.mina;


/**
 * 转账类响应对象
 * 
 * @author zhengwei
 */
public class TransferResponse extends AbstractCommonResponse {

    public TransferResponse(String transCode) {
        super(transCode);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3413135087316120228L;

    /**
     * 银行帐号
     */
    private String            bankAccount;

    /**
     * 交易所资金帐号
     */
    private String            fundAccount;

    /**
     * 货币代码
     */
    private String            moneyType;

    /**
     * 转账金额
     */
    private Long              transAmount;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

}
