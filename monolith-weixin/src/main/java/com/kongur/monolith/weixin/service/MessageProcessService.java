package com.kongur.monolith.weixin.service;

import com.kongur.monolith.weixin.domain.Message;
import com.kongur.monolith.weixin.domain.ProcessResult;

/**
 * 消息处理服务
 * <p>
 *  处理从平台收到的消息
 * </p>
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface MessageProcessService<M extends Message> {

    /**
     * 处理收到的消息数据
     * 
     * @param msg
     * @return
     */
    ProcessResult process(M msg);

}
