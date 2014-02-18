package com.kongur.monolith.im.weixin.service;

import java.util.Map;
import java.util.Map.Entry;

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

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.serivce.ApiService;
import com.kongur.monolith.im.serivce.ExecuteException;

/**
 * @author zhengwei
 * @date 2014-2-17
 */
@Service("defaultApiService")
public class DefaultApiService implements ApiService {

    private final Logger log = Logger.getLogger(getClass());

    public ServiceResult<String> executeGet(String apiUrl) throws ExecuteException {

        return this.executeGet(apiUrl, null);
    }

    /**
     * 执行get请求
     * 
     * @param apiUrl api url
     * @param getParams 请求参数，加在GET URL
     * @return
     */
    public ServiceResult<String> executeGet(String apiUrl, Map<String, String> getParams) throws ExecuteException {

        ServiceResult<String> result = new ServiceResult<String>();

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
            throw new ExecuteException("http get error", e);
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }

        result.setSuccess(true);
        result.setResult(retData);
        return result;
    }

    public ServiceResult<String> executePost(String apiUrl, String postParams) throws ExecuteException {

        ServiceResult<String> result = new ServiceResult<String>();

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
            throw new ExecuteException("http post error", e);
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }

        result.setSuccess(true);
        result.setResult(retData);

        return result;
    }

}
