package com.kongur.monolith.weixin.common.service;

/**
 * 订阅回复管理服务
 * 
 * @author zhengwei
 */
public interface RemoteSubscribeReplyService {

    /**
     * 微信平台会缓存订阅回复，所以后台修改后，需要通知微信平台刷新本地缓存
     */
    void refresh();

}
