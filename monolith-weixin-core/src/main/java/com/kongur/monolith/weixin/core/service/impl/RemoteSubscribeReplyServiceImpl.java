package com.kongur.monolith.weixin.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kongur.monolith.weixin.common.service.RemoteSubscribeReplyService;
import com.kongur.monolith.weixin.core.manager.SubscribeReplyManager;

/**
 * ¶©ÔÄ»Ø¸´·þÎñ
 * 
 * @author zhengwei
 */
@Service("remoteSubscribeReplyService")
public class RemoteSubscribeReplyServiceImpl implements RemoteSubscribeReplyService {

    @Autowired
    private SubscribeReplyManager subscribeReplyManager;

    @Override
    public void refresh() {
        subscribeReplyManager.refresh();
    }

}
