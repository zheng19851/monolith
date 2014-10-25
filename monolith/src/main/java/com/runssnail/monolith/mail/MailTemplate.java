package com.runssnail.monolith.mail;

import java.util.Map;

/**
 * 邮件模板
 * 
 * @author zhengwei
 *
 */
public interface MailTemplate {

    /**
     * 根据参数生成内容
     * 
     * @param model 参数
     * @return
     */
    String render(Map<String, ?> model);

}
