package com.kongur.monolith.weixin.core.service.reply;

import org.apache.log4j.Logger;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.common.domain.dto.Reply;
import com.kongur.monolith.weixin.core.common.Ordered;
import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * @author zhengwei
 * @date 2014年2月21日
 */
public abstract class AbstractReplyMessageBuilder<R extends Reply> implements ReplyMessageBuilder<R> {

    protected final Logger log   = Logger.getLogger(getClass());

    /**
     * 排序用
     */
    private int            order = Ordered.LOWEST_PRECEDENCE;

    public AbstractReplyMessageBuilder() {

    }

    @Override
    public Result<String> build(R reply, Message msg) {
        Result<String> result = new Result<String>();
        result.setSuccess(true);
        if (log.isDebugEnabled()) {
            log.debug("======build reply message start======");
        }

        preBuild(reply, msg, result);

        if (!result.isSuccess()) {
            return result;
        }

        doBuild(reply, msg, result);

        if (!result.isSuccess()) {
            return result;
        }

        postBuild(reply, msg, result);

        if (log.isDebugEnabled()) {
            log.debug("======build reply message end======");
        }

        return result;
    }

    protected abstract void doBuild(R reply, Message msg, Result<String> result);

    /**
     * 验证回复数据是否正确
     * 
     * @param reply
     * @param msg
     * @param result
     */
    protected void validate(R reply, Message msg, Result<String> result) {

    }

    /**
     * 后处理
     * 
     * @param reply
     * @param msg
     * @param result
     */
    protected void postBuild(R reply, Message msg, Result<String> result) {

    }

    /**
     * 预处理
     * 
     * @param reply
     * @param msg
     * @param result
     */
    protected void preBuild(R reply, Message msg, Result<String> result) {
        validate(reply, msg, result);

        if (!result.isSuccess()) {
            return;
        }
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
