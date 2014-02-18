package com.kongur.monolith.im.serivce;

import java.util.Map;

import net.sf.json.JSONObject;

import com.kongur.monolith.im.domain.ServiceResult;

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
    ServiceResult<String> executeGet(String apiUrl) throws ApiException;

    /**
     * 发起get请求
     * 
     * @param apiUrl 请求 api URL
     * @param getParams get请求参数
     * @return
     */
    ServiceResult<String> executeGet(String apiUrl, Map<String, String> getParams) throws ApiException;

    /**
     * 发起post请求
     * 
     * @param apiUrl 请求 api URL
     * @param postParams post参数
     * @return
     */
    ServiceResult<String> executePost(String apiUrl, String postParams) throws ApiException;

    /**
     * 发起get请求，返回结果为JSONObject
     * 
     * @param apiUrl
     * @return
     * @throws ApiException
     */
    ServiceResult<JSONObject> doGet(String apiUrl) throws ApiException;

    /**
     * 发起get请求，返回结果为JSONObject
     * 
     * @param apiUrl 请求 api URL
     * @param getParams get请求参数
     * @return
     */
    ServiceResult<JSONObject> doGet(String apiUrl, Map<String, String> getParams) throws ApiException;

    /**
     * 发起post请求，返回结果为JSONObject
     * 
     * @param apiUrl 请求 api URL
     * @param postParams post参数
     * @return
     */
    ServiceResult<JSONObject> doPost(String apiUrl, String postParams) throws ApiException;

}
