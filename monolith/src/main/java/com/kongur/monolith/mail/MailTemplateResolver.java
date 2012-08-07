package com.kongur.monolith.mail;

import java.util.Locale;


/**
 * 邮件模板查找工具， 根据locale来查找不同语言版本的邮件模板
 * 
 * @author zhengwei
 *
 */
public interface MailTemplateResolver {
    
    /**
     * 查找邮件模板
     * 
     * @param template 模板名称 test.vm
     * @param locale 
     * @return
     */
    MailTemplate resolveTemplate(String template, Locale locale);
}
