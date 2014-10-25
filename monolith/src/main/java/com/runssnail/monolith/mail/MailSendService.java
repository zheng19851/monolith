package com.runssnail.monolith.mail;

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
    SendResult send(MailDO mail) throws SendMailException;

    /**
     * 同步发送多封邮件
     * 
     * @param mails
     */
    SendResult send(MailDO[] mails) throws SendMailException;

    /**
     * 异步发送单封邮件
     * 
     * @param mail
     */
    SendResult asynSend(MailDO mail) throws SendMailException;

    /**
     * 异步发送多封邮件
     * 
     * @param mails
     */
    SendResult asynSend(MailDO[] mails) throws SendMailException;

}
