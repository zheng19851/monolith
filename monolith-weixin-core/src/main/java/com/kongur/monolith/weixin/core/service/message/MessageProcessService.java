package com.kongur.monolith.weixin.core.service.message;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.common.Ordered;
import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * 消息处理服务
 * <p>
 * 处理从平台收到的消息
 * </p>
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface MessageProcessService<M extends Message> extends Ordered {

    /**
     * 处理收到的消息数据
     * 
     * @param msg
     * @return result为返回给平台的响应数据
     */
    Result<String> process(M msg);

    /**
     * 是否支持处理当前消息
     * 
     * @param msg
     * @return
     */
    boolean supports(Message msg);

}
