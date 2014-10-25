package com.runssnail.monolith.mail.template.velocity;

import java.util.Locale;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.runssnail.monolith.mail.template.AbstractCachingMailTemplateResolver;
import com.runssnail.monolith.mail.template.MailTemplate;


/**
 * 查找velocity邮件模板
 * 
 * @author zhengwei
 */
public class VelocityMailTemplateResolver extends AbstractCachingMailTemplateResolver {

    private VelocityEngine      velocityEngine;

    private static final String TEMPLATE_SUFFIX = ".vm";

    @Override
    protected MailTemplate createMailTemplate(String templateName, Locale locale) {

        // 根据模板文件名称和语言来查找模板
        String finalName = findTemplateName(templateName, locale);

        Template template = velocityEngine.getTemplate(finalName);

        return new VelocityMailTemplate(template);
    }

    /**
     * 根据模板文件名称和语言来查找模板, test->test.vm or test_en_US.vm or test_zh_CN.vm
     * 
     * @param templateName 模板名称
     * @param locale 语言
     * @return
     */
    protected String findTemplateName(String templateName, Locale locale) {
        String retName = templateName;
        if (!retName.endsWith(TEMPLATE_SUFFIX)) {
            retName += retName + TEMPLATE_SUFFIX;
        }

        if (locale == null) {
            return retName;
        }

        StringBuilder sb = new StringBuilder(retName);
        String localeString = "_" + locale.getLanguage() + "_" + locale.getCountry();
        sb.insert(retName.length() - TEMPLATE_SUFFIX.length(), localeString);
        localeString = sb.toString();

        // 判断国际化模板文件是否存在, 存在则使用国际化模板，否者使用默认模板
        if (velocityEngine.resourceExists(localeString)) {
            return localeString;
        }

        return retName;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

}
