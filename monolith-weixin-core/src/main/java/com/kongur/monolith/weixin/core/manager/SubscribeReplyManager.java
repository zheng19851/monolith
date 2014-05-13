package com.kongur.monolith.weixin.core.manager;

import com.kongur.monolith.weixin.core.domain.SubscribeReplyDO;

/**
 * 关注事件消息回复设置管理
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
public interface SubscribeReplyManager {

    /**
     * 创建
     * 
     * @param reply
     * @return
     */
    String create(SubscribeReplyDO reply);

    /**
     * 查询
     * 
     * @param id
     * @return
     */
    SubscribeReplyDO getSubscribeReply();

    /**
     * 修改
     * 
     * @param reply
     * @return
     */
    boolean update(SubscribeReplyDO reply);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    boolean remove();

    void refresh();

}
