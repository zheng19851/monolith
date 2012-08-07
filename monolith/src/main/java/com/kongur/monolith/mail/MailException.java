package com.kongur.monolith.mail;

/**
 * 邮件发送异常基类
 * 
 * @author zhengwei
 */
public class MailException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1832675740761545323L;

    public MailException(String msg) {
        super(msg);
    }
    
    public MailException(Throwable cause) {
        super(cause);
    }

    public MailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
