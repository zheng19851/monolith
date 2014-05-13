package com.kongur.monolith.weixin.core.service.reply;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kongur.monolith.weixin.common.domain.dto.Reply;
import com.kongur.monolith.weixin.core.common.OrderComparator;

/**
 * 默认的 回复消息处理服务 查找服务
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
@Service("replyMessageBuilderResolver")
public class DefaultReplyMessageBuilderResolver implements ReplyMessageBuilderResolver<Reply>, ApplicationContextAware {

    private final Logger                     log = Logger.getLogger(getClass());

    private List<ReplyMessageBuilder<Reply>> replyMessageBuilders;

    /**
     * 默认 的创建器
     */
    @Resource(name = "discardReplyMessageBuilder")
    private ReplyMessageBuilder<Reply>       defaultReplyMessageBuilder;

    private ApplicationContext               applicationContext;

    @PostConstruct
    public void init() {

        Assert.notNull(this.defaultReplyMessageBuilder, "the default ReplyMessageBuilder can not be null.");

        if (this.replyMessageBuilders == null) {

            // 自动收集
            Map<String, ReplyMessageBuilder> builderMap = this.applicationContext.getBeansOfType(ReplyMessageBuilder.class);

            if (!builderMap.isEmpty()) {

                List<ReplyMessageBuilder<Reply>> builders = new ArrayList<ReplyMessageBuilder<Reply>>(builderMap.size());

                for (Entry<String, ReplyMessageBuilder> entry : builderMap.entrySet()) {
                    builders.add(entry.getValue());
                }

                this.replyMessageBuilders = builders;

                if (log.isInfoEnabled()) {
                    log.info("auto-find " + builderMap.size() + " count of ReplyMessageBuilders.");
                }

            } else {

                log.warn("can not auto-find any ReplyMessageBuilders.");
            }
        } else {

            if (log.isInfoEnabled()) {
                log.info("there are " + this.replyMessageBuilders.size() + " count of ReplyMessageBuilders.");
            }

        }

        if (this.replyMessageBuilders != null) {
            OrderComparator.sort(this.replyMessageBuilders);
        }

    }

    @Override
    public ReplyMessageBuilder<Reply> resolve(Reply reply) {

        ReplyMessageBuilder<Reply> replyMessageBuilder = null;

        for (ReplyMessageBuilder<Reply> builder : replyMessageBuilders) {
            if (builder.supports(reply)) {
                if (log.isDebugEnabled()) {
                    log.debug("find ReplyMessageBuilder, name=" + builder.getClass().getSimpleName());
                }
                replyMessageBuilder = builder;
                break;
            }
        }

        if (replyMessageBuilder == null) {
            if (log.isDebugEnabled()) {
                log.debug("can not find a ReplyMessageBuilder, so will use the default ReplyMessageBuilder="
                          + this.defaultReplyMessageBuilder.getClass().getSimpleName());
            }

            replyMessageBuilder = this.defaultReplyMessageBuilder;
        }

        return replyMessageBuilder;
    }

    public List<ReplyMessageBuilder<Reply>> getReplyMessageBuilders() {
        return replyMessageBuilders;
    }

    public void setReplyMessageBuilders(List<ReplyMessageBuilder<Reply>> replyMessageBuilders) {
        this.replyMessageBuilders = replyMessageBuilders;
    }

    public ReplyMessageBuilder<Reply> getDefaultReplyMessageBuilder() {
        return defaultReplyMessageBuilder;
    }

    public void setDefaultReplyMessageBuilder(ReplyMessageBuilder<Reply> defaultReplyMessageBuilder) {
        this.defaultReplyMessageBuilder = defaultReplyMessageBuilder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

}
