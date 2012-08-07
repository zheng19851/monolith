package com.kongur.monolith.mail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 支持邮件模板缓存
 * 
 * @author zhengwei
 */
public abstract class AbstractCachingMailTemplateResolver implements MailTemplateResolver {

    private Map<Object, MailTemplate> mailTemplateCache = new HashMap<Object, MailTemplate>();

    @Override
    public MailTemplate resolveTemplate(String template, Locale locale) {

        Object cacheKey = getCacheKey(template, locale);

        synchronized (this.mailTemplateCache) {
            MailTemplate mailTemplate = mailTemplateCache.get(cacheKey);
            if (mailTemplate == null) {
                mailTemplate = createMailTemplate(template, locale);
                mailTemplateCache.put(cacheKey, mailTemplate);
            }

            return mailTemplate;
        }

    }

    protected Object getCacheKey(String template, Locale locale) {
        if(locale == null) {
            return template;
        }
        
        return template + "_" + locale;
    }

    /**
     * 创建邮件模板对象, 根据模板文件名称和语言来查找
     * 
     * @param template
     * @param locale
     * @return
     */
    protected abstract MailTemplate createMailTemplate(String template, Locale locale);

}
