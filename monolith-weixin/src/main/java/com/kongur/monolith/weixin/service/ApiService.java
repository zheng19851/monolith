package com.kongur.monolith.weixin.service;

import java.util.Map;

import com.kongur.monolith.weixin.domain.ServiceResult;

/**
 * 平台API服务
 * 
 * @author zhengwei
 * @date 2014-2-17
 */
public interface ApiService {

    /**
     * 发起get请求
     * 
     * @param apiUrl 请求 api URL
     * @return
     */
    ServiceResult<String> executeGet(String apiUrl) throws ExecuteException;

    /**
     * 发起get请求
     * 
     * @param apiUrl 请求 api URL
     * @param getParams get请求参数
     * @return
     */
    ServiceResult<String> executeGet(String apiUrl, Map<String, String> getParams) throws ExecuteException;

    /**
     * 发起post请求
     * 
     * @param apiUrl 请求 api URL
     * @param postParams post参数
     * @return
     */
    ServiceResult<String> executePost(String apiUrl, String postParams) throws ExecuteException;
}
