package com.kongur.monolith.im.serivce;

import com.kongur.monolith.im.domain.Message;
import com.kongur.monolith.im.domain.ServiceResult;

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
    ServiceResult<String> process(M msg);

}
