package com.kongur.monolith.mail;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kongur.monolith.mail.template.MailTemplate;
import com.kongur.monolith.mail.template.MailTemplateResolver;

/**
 * 邮件发送服务
 *
 * @author zhengwei
 */
public abstract class AbstractMailSendService implements MailSendService {

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * 异步执行器
     */
    private Executor executor;

    /**
     * 创建邮件模板
     */
    private MailTemplateResolver mailTemplateResolver;

    /**
     * 默认的邮件发送者
     */
    private String defaultMailFrom;

    /**
     * 是否禁止发送邮件，默认不禁止
     */
    private boolean disableSendMail = false;

    /**
     * 初始化
     */
    public void init() {
        if (this.executor == null) {
            this.executor = Executors.newSingleThreadExecutor();
        }

        if (StringUtils.isBlank(this.defaultMailFrom)) {
            throw new RuntimeException("the defaultMailFrom can not be blank!");
        }

    }

    @Override
    public SendResult send(MailDO mail) throws SendMailException {

        return send(new MailDO[]{mail});
    }

    /**
     * 生成邮件内容
     *
     * @param mail
     */
    protected void renderContent(MailDO mail, SendResult result) {

        if (StringUtils.isBlank(mail.getTemplate()) && StringUtils.isBlank(mail.getContent())) {
            throw new SendMailException("The MailDO's template or content can not be blank!");
        }

        if (StringUtils.isNotBlank(mail.getTemplate())) {
            // 创建邮件模板
            MailTemplate mailTemplate = mailTemplateResolver.resolveTemplate(mail.getTemplate(), mail.getLocale());

            String content = mailTemplate.render(mail.getParams());

            mail.setContent(content);

        }

    }

    @Override
    public SendResult send(final MailDO[] mails) throws SendMailException {
        SendResult result = new SendResult(true);

        if (disableSendMail) {
            return result;
        }

        for (MailDO mail : mails) {
            sendInternal(mail, result);
            if (!result.isSuccess()) {
                return result;
            }
        }

        return result;
    }

    private void sendInternal(MailDO mail, SendResult result) {
        if (StringUtils.isBlank(mail.getFrom())) {
            mail.setFrom(this.defaultMailFrom);
        }

        try {

            renderContent(mail, result);

            if (!result.isSuccess()) {
                logger.error("send mail error, resultInfo=" + result.getResultInfo() + ", MailDO=" + mail);
                return;
            }

            doSend(mail, result);

            if (logger.isDebugEnabled()) {
                logger.debug("send mail success.MailDO=" + mail);
            }

        } catch (Exception e) {
            logger.error("send mail error, MailDO=" + mail, e);
            throw new SendMailException(e);
        }

    }

    @Override
    public SendResult asynSend(final MailDO mail) throws SendMailException {
        return asynSend(new MailDO[]{mail});
    }

    @Override
    public SendResult asynSend(final MailDO[] mails) throws SendMailException {

        SendResult result = new SendResult(true);

        if (disableSendMail) {
            return result;
        }

        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    send(mails);
                } catch (Exception e) {
                    logger.error("send mail error", e);
                }
            }
        });

        return result;
    }

    /**
     * sub class to impl
     *
     * @param mail
     * @param result
     */
    protected abstract void doSend(MailDO mail, SendResult result) throws Exception;

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public MailTemplateResolver getMailTemplateResolver() {
        return mailTemplateResolver;
    }

    public void setMailTemplateResolver(MailTemplateResolver mailTemplateResolver) {
        this.mailTemplateResolver = mailTemplateResolver;
    }

    public void setDisableSendMail(boolean disableSendMail) {
        this.disableSendMail = disableSendMail;
    }

    public boolean isDisableSendMail() {
        return disableSendMail;
    }

    public String getDefaultMailFrom() {
        return defaultMailFrom;
    }

    public void setDefaultMailFrom(String defaultMailFrom) {
        this.defaultMailFrom = defaultMailFrom;
    }

}
