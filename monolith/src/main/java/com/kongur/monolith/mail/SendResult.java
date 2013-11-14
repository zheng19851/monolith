package com.kongur.monolith.mail;

import com.kongur.monolith.common.result.Result;

/**
 * 发送邮件结果
 * 
 * @author zhengwei
 *
 */
public class SendResult extends Result {

    public SendResult(boolean success) {
        super(success);
    }

    public SendResult() {
    }

    /**
     * 
     */
    private static final long serialVersionUID = -2721975115652157379L;

}
