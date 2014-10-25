package com.runssnail.monolith.mail;

/**
 * 邮件发送异常基类
 * 
 * @author zhengwei
 */
public class SendMailException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1832675740761545323L;

    public SendMailException(String msg) {
        super(msg);
    }
    
    public SendMailException(Throwable cause) {
        super(cause);
    }

    public SendMailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
