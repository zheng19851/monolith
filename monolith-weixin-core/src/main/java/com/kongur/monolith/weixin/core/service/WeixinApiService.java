package com.kongur.monolith.weixin.core.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.kongur.monolith.common.result.Result;


/**
 *
 *weixin api service
 *
 *@author zhengwei
 *
 *@date 2014年2月26日	
 *
 */
public interface WeixinApiService {
    
    
    /**
     * 发起get请求，默认加上access_token，返回结果为JSONObject
     * 
     * @param apiUrl
     * @return
     * @throws ApiException
     */
    Result<JSONObject> doGet(String apiUrl) throws ApiException;
    
    /**
     * 发起get请求，返回结果为JSONObject
     * 
     * @param apiUrl
     * @return
     * @throws ApiException
     */
    Result<JSONObject> doGet(String apiUrl, boolean appendAccessToken) throws ApiException;

    /**
     * 发起get请求，返回结果为JSONObject
     * 
     * @param apiUrl 请求 api URL
     * @param getParams get请求参数
     * @return
     */
    Result<JSONObject> doGet(String apiUrl, Map<String, String> getParams, boolean appendAccessToken) throws ApiException;

    /**
     * 发起post请求，返回结果为JSONObject
     * 
     * @param apiUrl 请求 api URL
     * @param postParams post参数
     * @return
     */
    Result<JSONObject> doPost(String apiUrl, String postParams, boolean appendAccessToken) throws ApiException;
    
    
    /**
     * 发起post请求，默认会加上access_token，返回结果为JSONObject
     * 
     * @param apiUrl 请求 api URL
     * @param postParams post参数
     * @return
     */
    Result<JSONObject> doPost(String apiUrl, String postParams) throws ApiException;

}
