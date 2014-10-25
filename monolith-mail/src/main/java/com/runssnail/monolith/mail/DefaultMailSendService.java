package com.runssnail.monolith.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.runssnail.monolith.mail.AbstractMailSendService;
import com.runssnail.monolith.mail.MailDO;
import com.runssnail.monolith.mail.SendResult;

/**
 * 默认的邮件发送实现
 * 
 * @author zhengwei
 */
public class DefaultMailSendService extends AbstractMailSendService {

    private JavaMailSender javaMailSender;

    @Override
    protected void doSend(MailDO mail, SendResult result) throws Exception {

        javaMailSender.send(createMimeMessage(mail));

    }

    private MimeMessage createMimeMessage(MailDO mail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        if (mail.getFrom() != null) {
            helper.setFrom(mail.getFrom());
        }

        if (mail.getReplyTo() != null) {
            helper.setReplyTo(mail.getReplyTo());
        }

        helper.setTo(mail.getTo());

        if (mail.getSubject() != null) {
            helper.setSubject(mail.getSubject());
        }

        if (mail.getContent() != null) {
            if (mail.isHtml()) {
                helper.setText(mail.getContent(), true);
            } else {
                helper.setText(mail.getContent());
            }
        }

        return message;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

}
