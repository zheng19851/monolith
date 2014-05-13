package com.kongur.monolith.weixin.core.service.impl;

import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.service.AccessTokenService;
import com.kongur.monolith.weixin.core.service.ApiException;
import com.kongur.monolith.weixin.core.service.ApiService;
import com.kongur.monolith.weixin.core.service.WeixinApiService;
import com.kongur.monolith.weixin.core.utils.WeixinApiHelper;

/**
 * 可重试的ApiService，当access_token过期时，默认会重试一次
 * 
 * @author zhengwei
 * @date 2014年2月25日
 * @see WeixinApiService
 */
// @Service("retryApiService")
@Deprecated
public class RetryApiServiceImpl implements ApiService {

    private final Logger       log            = Logger.getLogger(getClass());

    @Resource(name = "defaultApiService")
    private ApiService         apiService;

    /**
     * 默认重试一次
     */
    private int                retryCount     = 1;

    @Resource(name = "defaultAccessTokenService")
    private AccessTokenService accessTokenService;

    /**
     * access_token
     */
    private String             accessTokenKey = "access_token";

    @Override
    public Result<String> executeGet(String apiUrl) throws ApiException {
        return apiService.executeGet(apiUrl);
    }

    @Override
    public Result<String> executeGet(String apiUrl, Map<String, String> getParams) throws ApiException {
        return apiService.executeGet(apiUrl, getParams);
    }

    @Override
    public Result<String> executePost(String apiUrl, String postParams) throws ApiException {
        return apiService.executePost(apiUrl, postParams);
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl) throws ApiException {
        return doGet(apiUrl, null);
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl, final Map<String, String> getParams) throws ApiException {
        // Result<JSONObject> result = null;
        //
        // String internalApiUrl = appendAccessToken(apiUrl, accessTokenService.getAccessToken());
        //
        // // 先执行一次
        // result = apiService.doGet(internalApiUrl, getParams);
        //
        // if (!supports(result)) {
        // return result;
        // }
        //
        // for (int i = 0; i < retryCount; i++) {
        //
        // // 先刷新
        // Result<String> refreshResult = accessTokenService.refresh();
        // if (!refreshResult.isSuccess()) {
        // return result;
        // }
        //
        // // 刷新后的access_token
        // internalApiUrl = appendAccessToken(apiUrl, refreshResult.getResult());
        //
        // result = apiService.doGet(internalApiUrl, getParams);
        //
        // if (!supports(result)) {
        // return result;
        // }
        //
        // }
        //
        // return result;

        return RETRY_TEMPLATE.execute(apiUrl, getParams, new Callback() {

            @Override
            public Result<JSONObject> doAction(String internalApiUrl) {
                return apiService.doGet(internalApiUrl, getParams);
            }
        });
    }

    private String appendAccessToken(String apiUrl, String accessToken) {
        String internalApiUrl = apiUrl;

        // 如果没有 就直接返回
        if (!apiUrl.contains(this.accessTokenKey)) {
            return internalApiUrl;
        }

        // 加上access_token参数
        // internalApiUrl = internalApiUrl + "&" + this.accessTokenKey + accessToken;

        return MessageFormat.format(internalApiUrl, accessToken);
    }

    /**
     * 可重试原因，出错了&&是access_token过期
     * 
     * @param result
     * @return
     */
    protected boolean supports(Result<JSONObject> result) {
        return !result.isSuccess() && WeixinApiHelper.isAccessTokenInvalid(result.getResult());
    }

    private final RetryTemplate RETRY_TEMPLATE = new RetryTemplate();

    @Override
    public Result<JSONObject> doPost(String apiUrl, final String postParams) throws ApiException {

        // Result<JSONObject> result = null;
        //
        // String internalApiUrl = appendAccessToken(apiUrl, accessTokenService.getAccessToken());
        //
        // // 先执行一次
        // result = apiService.doPost(internalApiUrl, postParams);
        //
        // if (!supports(result)) {
        // return result;
        // }
        //
        // for (int i = 0; i < retryCount; i++) {
        //
        // // 先刷新
        // Result<String> refreshResult = accessTokenService.refresh();
        // if (!refreshResult.isSuccess()) {
        // return result;
        // }
        //
        // // 刷新后的access_token
        // internalApiUrl = appendAccessToken(apiUrl, refreshResult.getResult());
        //
        // result = apiService.doPost(internalApiUrl, postParams);
        //
        // if (!supports(result)) {
        // return result;
        // }
        //
        // }
        //
        // return result;

        return RETRY_TEMPLATE.execute(apiUrl, postParams, new Callback() {

            @Override
            public Result<JSONObject> doAction(String internalApiUrl) {
                return apiService.doPost(internalApiUrl, postParams);
            }
        });
    }

    protected boolean supports(Exception e) {
        // 你妹的，测试的时候经常会出java.net.UnknownHostException: api.weixin.qq.com
        return e.getCause() instanceof UnknownHostException;
    }

    private class RetryTemplate {

        <T> Result<JSONObject> execute(String apiUrl, T t, Callback callback) {
            Result<JSONObject> result = null;

            String internalApiUrl = apiUrl;

            // 有access_token
            if (apiUrl.contains(accessTokenKey)) {
                internalApiUrl = appendAccessToken(apiUrl, accessTokenService.getAccessToken());
            }

            try {
                // 先执行一次
                result = callback.doAction(internalApiUrl);

                if (!supports(result)) {
                    return result;
                }
            } catch (ApiException e) {
                // 你妹的，测试的时候经常会出java.net.UnknownHostException: api.weixin.qq.com
                if (!(e.getCause() instanceof UnknownHostException)) { // 是这个错误么，就重试..
                    throw e;
                }
            }

            for (int i = 0; i < retryCount; i++) {

                log.warn("retry execute api (" + i + "), previous result=" + result);

                // 前面是access_token错误才需要刷新
                if (WeixinApiHelper.isAccessTokenInvalid(result.getResult())) {
                    // 先刷新
                    Result<String> refreshResult = accessTokenService.refresh();
                    if (!refreshResult.isSuccess()) {
                        return result;
                    }

                    // 刷新后的access_token
                    internalApiUrl = appendAccessToken(apiUrl, refreshResult.getResult());
                }

                try {

                    result = callback.doAction(internalApiUrl);

                    if (!supports(result)) {
                        return result;
                    }
                } catch (ApiException e) {
                    if (!supports(e)) {
                        throw e;
                    }
                }

            }

            return result;

        }

    }

    interface Callback {

        Result<JSONObject> doAction(String internalApiUrl);
    }

    public static void main(String[] args) {
        System.out.println(MessageFormat.format("fjalfjl111199999f3933kjkj{1}", "hello"));
    }

}
