package com.runssnail.monolith.mail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 邮件数据对象
 * 
 * @author zhengwei
 */
public class MailDO implements Serializable {

    /**
     * 
     */
    private static final long   serialVersionUID = -4306347729988226975L;

    /**
     * 发送人
     */
    private String              from;

    /**
     * 收件人
     */
    private String[]            to;

    /**
     * 答复
     */
    private String              replyTo;

    /**
     * 主题
     */
    private String              subject;

    /**
     * 邮件模板
     */
    private String              template;

    /**
     * 参数
     */
    private Map<String, Object> params;

    /**
     * 是否HTML内容
     */
    private boolean             html;

    /**
     * 内容, 可通过template来生成，如果外部设置了content, 那么不会通过template来生成内容
     */
    private String              content;

    private Locale              locale;

    public MailDO(String to, String subject, String template) {
        this(null, to, subject, template);
    }

    public MailDO(String from, String to, String subject, String template) {
        this.from = from;
        this.to = new String[] { to };
        this.subject = subject;
        this.template = template;
    }

    public MailDO(String from, String[] to, String subject, String template) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.template = template;
    }

    public Locale getLocale() {
        return locale;
    }

    public MailDO setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailDO setContent(String content) {
        this.content = content;
        return this;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public MailDO setReplyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public MailDO setFrom(String from) {
        this.from = from;
        return this;
    }

    public String[] getTo() {
        return to;
    }

    public MailDO setTo(String[] to) {
        this.to = to;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public MailDO setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public MailDO setTemplate(String template) {
        this.template = template;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public MailDO setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public boolean isHtml() {
        return html;
    }

    public MailDO setHtml(boolean html) {
        this.html = html;
        return this;
    }

    /**
     * 添加参数
     * 
     * @param key
     * @param val
     * @return
     */
    public MailDO addParam(String key, Object val) {
        if (this.params == null) {
            params = new HashMap<String, Object>();
        }

        params.put(key, val);
        return this;
    }

    /**
     * 添加参数
     */
    public MailDO addParams(Map<String, Object> params) {
        if (this.params == null) {
            this.params = new HashMap<String, Object>();
        }

        this.params.putAll(params);
        return this;
    }

}
