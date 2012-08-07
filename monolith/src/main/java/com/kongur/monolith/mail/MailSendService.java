package com.kongur.monolith.mail;

/**
 * 邮件发送服务
 * 
 * @author zhengwei
 */
public interface MailSendService {

    /**
     * 同步发送单封邮件
     * 
     * @param mail
     */
    void synSend(MailDO mail) throws MailException;

    /**
     * 同步发送多封邮件
     * 
     * @param mails
     */
    void synSend(MailDO[] mails) throws MailException;

    /**
     * 异步发送单封邮件
     * 
     * @param mail
     */
    void asynSend(MailDO mail) throws MailException;

    /**
     * 异步发送多封邮件
     * 
     * @param mails
     */
    void asynSend(MailDO[] mails) throws MailException;

}
