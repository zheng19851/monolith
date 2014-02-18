package com.kongur.monolith.im.serivce;

import com.kongur.monolith.im.domain.ServiceResult;

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
    ServiceResult<String> refresh();

    /**
     * 获取AccessToken
     * 
     * @return
     */
    String getAccessToken();

}
