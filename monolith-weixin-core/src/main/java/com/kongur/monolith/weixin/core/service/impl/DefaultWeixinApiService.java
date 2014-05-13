package com.kongur.monolith.weixin.core.service.impl;

import java.net.UnknownHostException;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.service.AccessTokenService;
import com.kongur.monolith.weixin.core.service.ApiException;
import com.kongur.monolith.weixin.core.service.ApiService;
import com.kongur.monolith.weixin.core.service.WeixinApiService;
import com.kongur.monolith.weixin.core.utils.WeixinApiHelper;

/**
 * 默认的微信 api 服务接口，提供重试功能，以及免设置access_token
 * 
 * @author zhengwei
 * @date 2014年2月26日
 */
@Service("defaultWeixinApiService")
public class DefaultWeixinApiService implements WeixinApiService {

    private final Logger              log            = Logger.getLogger(getClass());

    @Resource(name = "defaultApiService")
    private ApiService                apiService;

    private final RetryTemplate       retryTemplate  = new RetryTemplate();

    /**
     * 替换变量名用
     */
    @Autowired
    private PropertyPlaceholderHelper propertyPlaceholderHelper;

    /**
     * 默认重试一次
     */
    private int                       retryCount     = 1;

    @Resource(name = "defaultAccessTokenService")
    private AccessTokenService        accessTokenService;

    /**
     * access_token_key
     */
    private String                    accessTokenKey = "access_token";

    /**
     * 重试等待间隔，默认300毫秒
     */
    @Value("${weixin.api.retry.period}")
    private long                      retryWait      = 300;

    protected boolean supports(Exception e) {
        // 你妹的，测试的时候经常会出java.net.UnknownHostException: api.weixin.qq.com
        return e.getCause() instanceof UnknownHostException;
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl, boolean replaceAccessToken) throws ApiException {
        return this.doGet(apiUrl, null, replaceAccessToken);
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl, final Map<String, String> getParams, boolean replaceAccessToken)
                                                                                                                         throws ApiException {
        return retryTemplate.execute(apiUrl, replaceAccessToken, new Callback() {

            @Override
            public Result<JSONObject> doAction(String internalApiUrl) {
                return apiService.doGet(internalApiUrl, getParams);
            }
        });
    }

    @Override
    public Result<JSONObject> doPost(String apiUrl, final String postParams, boolean replaceAccessToken)
                                                                                                              throws ApiException {
        return retryTemplate.execute(apiUrl, replaceAccessToken, new Callback() {

            @Override
            public Result<JSONObject> doAction(String internalApiUrl) {
                return apiService.doPost(internalApiUrl, postParams);
            }
        });
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl) throws ApiException {
        return this.doGet(apiUrl, true);
    }

    @Override
    public Result<JSONObject> doPost(String apiUrl, String postParams) throws ApiException {
        return doPost(apiUrl, postParams, true);
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

    private String appendAccessToken(String apiUrl, String accessToken) {
        String internalApiUrl = apiUrl;

        // 如果没有 就直接返回
        // if (!apiUrl.contains(this.accessTokenKey)) {
        // return internalApiUrl;
        // }

        if (!internalApiUrl.contains("?")) {
            internalApiUrl = internalApiUrl + "?";
        }

        // 有别的参数
        if (internalApiUrl.contains("=")) {
            internalApiUrl = internalApiUrl + "&";
        }

        // 加上access_token参数
        internalApiUrl = internalApiUrl + this.accessTokenKey + "=" + accessToken;

        return internalApiUrl;
    }

    /**
     * 重试模板
     * 
     * @author zhengwei
     */
    private class RetryTemplate {

        public Result<JSONObject> execute(String apiUrl, boolean replaceAccessToken, Callback callback) {
            Result<JSONObject> result = new Result<JSONObject>();

            String internalApiUrl = apiUrl;

            if (replaceAccessToken) {
                // internalApiUrl = appendAccessToken(apiUrl, accessTokenService.getAccessToken());
                internalApiUrl = replaceAccessTokenKey(apiUrl, accessTokenService.getAccessToken());
            }

            // result = executeInternal(internalApiUrl, callback);

            try {

                result = callback.doAction(internalApiUrl);

                if (!supports(result)) {
                    return result;
                }

            } catch (ApiException e) {
                // 你妹的，测试的时候经常会出java.net.UnknownHostException: api.weixin.qq.com
                if (!supports(e)) { // 是这个错误么，就重试..
                    throw e;
                }
            }

            for (int i = 1; i <= retryCount; i++) {

                log.warn("retry execute api (" + i + "), previous result=" + result);

                // 等待一会再重试，间隔太短，没什么效果
                try {
                    Thread.sleep(retryWait);
                } catch (InterruptedException e) {
                    log.warn("retry wait is interrupted,", e);
                }

                // 前面是access_token错误才需要刷新
                if (replaceAccessToken && WeixinApiHelper.isAccessTokenInvalid(result.getResult())) {
                    // 先刷新
                    Result<String> refreshResult = accessTokenService.refresh();
                    if (!refreshResult.isSuccess()) {
                        return result;
                    }

                    // 刷新后的access_token
                    // internalApiUrl = appendAccessToken(apiUrl, refreshResult.getResult());
                    internalApiUrl = replaceAccessTokenKey(apiUrl, refreshResult.getResult());

                }

                // result = executeInternal(internalApiUrl, callback);

                try {

                    result = callback.doAction(internalApiUrl);
                    if (!supports(result)) {
                        return result;
                    }

                } catch (ApiException e) {
                    // 你妹的，测试的时候经常会出java.net.UnknownHostException: api.weixin.qq.com
                    if (!supports(e)) { // 是这个错误么，就重试..
                        throw e;
                    }
                }

            }

            return result;
        }

        /**
         * 替换AccessTokenKey占位符
         * 
         * @param apiUrl
         * @return
         */
        private String replaceAccessTokenKey(String apiUrl, final String accessToken) {
            String replacedApiUrl = propertyPlaceholderHelper.replacePlaceholders(apiUrl, new PlaceholderResolver() {

                @Override
                public String resolvePlaceholder(String placeholderName) {
                    if (accessTokenKey.equals(placeholderName)) {
                        return accessToken;
                    }
                    return null;
                }
            });

            return replacedApiUrl;
        }

        /**
         * 执行
         * 
         * @param internalApiUrl
         * @param callback
         * @return
         */
        private Result<JSONObject> executeInternal(String internalApiUrl, Callback callback) {

            Result<JSONObject> result = new Result<JSONObject>();
            try {

                result = callback.doAction(internalApiUrl);

            } catch (ApiException e) {
                // 你妹的，测试的时候经常会出java.net.UnknownHostException: api.weixin.qq.com
                if (!supports(e)) { // 是这个错误么，就重试..
                    throw e;
                }
            }

            return result;
        }

    }

    interface Callback {

        Result<JSONObject> doAction(String internalApiUrl);
    }

    public long getRetryWait() {
        return retryWait;
    }

    public void setRetryWait(long retryWait) {
        this.retryWait = retryWait;
    }

}
