package com.kongur.monolith.weixin.core.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.service.ApiException;
import com.kongur.monolith.weixin.core.service.ApiService;
import com.kongur.monolith.weixin.core.utils.WeixinApiHelper;

/**
 * 微信API接口服务
 * 
 * @author zhengwei
 * @date 2014-2-17
 */
@Service("defaultApiService")
public class DefaultApiService implements ApiService {

    private final Logger log = Logger.getLogger(getClass());

    public Result<String> executeGet(String apiUrl) throws ApiException {

        return this.executeGet(apiUrl, null);
    }

    /**
     * 执行get请求
     * 
     * @param apiUrl api url
     * @param getParams 请求参数，加在GET URL
     * @return
     */
    public Result<String> executeGet(String apiUrl, Map<String, String> getParams) throws ApiException {

        if (log.isDebugEnabled()) {
            log.debug("invoke executeGet, apiUrl=" + apiUrl + ", getParams=" + getParams);
        }

        final Result<String> result = new Result<String>();

        String retData = null;

        String internalApiUrl = apiUrl;

        if (getParams != null && !getParams.isEmpty()) {
            StringBuilder sb = new StringBuilder(internalApiUrl);
            for (Entry<String, String> entry : getParams.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(internalApiUrl);
        try {

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                log.error("request error, There is no response data. apiUrl=" + internalApiUrl);
                result.setError("9001", "There is no response data");
                return result;
            }

            retData = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity); // Consume response content
            if (log.isInfoEnabled()) {
                log.info("get response=" + retData);
            }

        } catch (Exception e) {
            log.error("http get error, apiUrl=" + internalApiUrl, e);
            throw new ApiException("http get error", e);
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }

        if (log.isDebugEnabled()) {
            log.debug("invoke executeGet successfully");
        }

        result.setSuccess(true);
        result.setResult(retData);
        return result;
    }

    public Result<String> executePost(String apiUrl, String postParams) throws ApiException {

        if (log.isDebugEnabled()) {
            log.debug("invoke executePost, apiUrl=" + apiUrl + ", postParams=" + postParams);
        }

        final Result<String> result = new Result<String>();

        String internalApiUrl = apiUrl;

        String retData = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(internalApiUrl);
        httpPost.setEntity(new StringEntity(postParams, "UTF-8"));

        try {
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                log.error("request error, There is no response data. apiUrl=" + internalApiUrl);
                result.setError("9001", "There is no response data");
                return result;
            }

            retData = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity); // Consume response content
            if (log.isInfoEnabled()) {
                log.info("post response=" + retData);
            }

        } catch (Exception e) {
            log.error("http post error, apiUrl=" + internalApiUrl, e);
            throw new ApiException("http post error", e);
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }

        if (log.isDebugEnabled()) {
            log.debug("invoke executePost successfully");
        }

        result.setSuccess(true);
        result.setResult(retData);

        return result;
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl) throws ApiException {
        return this.doGet(apiUrl, null);
    }

    @Override
    public Result<JSONObject> doGet(String apiUrl, Map<String, String> getParams) throws ApiException {

        final Result<JSONObject> result = new Result<JSONObject>();

        Result<String> dataResult = executeGet(apiUrl, getParams);

        if (!dataResult.isSuccess()) {
            result.setError(dataResult.getResultCode(), dataResult.getResultInfo());
            return result;
        }

        JSONObject jsonObj = JSONObject.fromObject(dataResult.getResult());
        result.setResult(jsonObj);

        if (!WeixinApiHelper.isSuccess(jsonObj)) {
            result.setError(WeixinApiHelper.getErrCode(jsonObj), WeixinApiHelper.getErrMsg(jsonObj));
            return result;
        }

        result.setSuccess(true);
        return result;
    }

    @Override
    public Result<JSONObject> doPost(String apiUrl, String postParams) throws ApiException {

        if (log.isDebugEnabled()) {
            log.debug("do post data, apiUrl=" + apiUrl + ", postParams=" + postParams);
        }

        final Result<JSONObject> result = new Result<JSONObject>();

        Result<String> dataResult = executePost(apiUrl, postParams);

        if (!dataResult.isSuccess()) {
            log.error("do post data error, apiUrl=" + apiUrl + ", postParams=" + postParams + ", errcode="
                      + dataResult.getResultCode() + ", errmsg=" + dataResult.getResultInfo());
            result.setError(dataResult.getResultCode(), dataResult.getResultInfo());
            return result;
        }

        JSONObject jsonObj = JSONObject.fromObject(dataResult.getResult());
        result.setResult(jsonObj);

        if (!WeixinApiHelper.isSuccess(jsonObj)) {

            String errCode = WeixinApiHelper.getErrCode(jsonObj);
            String errMsg = WeixinApiHelper.getErrMsg(jsonObj);
            log.error("do post data error, apiUrl=" + apiUrl + ", postParams=" + postParams + ", errcode=" + errCode
                      + ", errmsg=" + errMsg);
            result.setError(errCode, errMsg);
            return result;
        }

        if (log.isDebugEnabled()) {
            log.debug("do post data success, apiUrl=" + apiUrl + ", postParams=" + postParams + ", result="
                      + dataResult.getResult());
        }

        result.setSuccess(true);
        return result;

    }

}
