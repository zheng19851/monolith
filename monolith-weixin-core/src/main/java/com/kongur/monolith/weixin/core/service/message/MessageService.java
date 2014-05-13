package com.kongur.monolith.weixin.core.service.message;

import com.kongur.monolith.weixin.core.domain.message.Message;

/**
 * 消息管理服务
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public interface MessageService {

    /**
     * 保存
     * 
     * @param msg
     * @return 内部用的消息唯一ID
     */
    String store(Message msg);

    /**
     * 是否存在
     * 
     * @param msg
     * @return
     */
    boolean contains(Message msg);

}
