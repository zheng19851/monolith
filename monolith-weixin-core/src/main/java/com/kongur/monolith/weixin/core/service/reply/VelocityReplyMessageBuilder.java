package com.kongur.monolith.weixin.core.service.reply;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.common.domain.dto.Reply;
import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * 用Velocity实现的回复消息创建器
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
public abstract class VelocityReplyMessageBuilder<R extends Reply> extends AbstractReplyMessageBuilder<R> {

    private VelocityEngine velocityEngine;

    /**
     * 模板路径
     */
    private String         template;

    /**
     * 创建回复数据
     * 
     * @param reply
     * @param msg
     * @param result
     */
    protected void doBuild(R reply, Message msg, Result<String> result) {

        Map model = new HashMap();
        buildDefaultModelParams(reply, msg, model);
        buildModelParams(reply, msg, model);
        String buildResult = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, getTemplate(), model);
        if (log.isDebugEnabled()) {
            log.debug("build reply content successfully, content=" + buildResult);
        }

        result.setResult(buildResult);
    }

    /**
     * 设置默认的velocity参数
     * 
     * @param reply
     * @param msg
     * @param model
     */
    protected void buildDefaultModelParams(R reply, Message msg, Map model) {
        model.put("toUser", msg.getFromUserName());
        model.put("fromUser", msg.getToUserName());

        model.put("createTime", new Date().getTime());

        model.put("reply", reply);

    }

    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * 模板
     * 
     * @return
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * 参数
     * 
     * @param reply
     * @param msg
     * @return
     */
    protected abstract void buildModelParams(R reply, Message msg, Map model);

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

}
