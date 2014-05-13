package com.kongur.monolith.weixin.core.service;

import com.kongur.monolith.common.result.Result;

/**
 * AccessToken管理服务
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface AccessTokenService {

    /**
     * 刷新 access token
     * 
     * @return
     */
    Result<String> refresh();

    /**
     * 获取AccessToken
     * 
     * @return
     */
    String getAccessToken();

}
