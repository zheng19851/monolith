package com.kongur.monolith.weixin.service.impl;

import java.text.MessageFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.domain.ServiceResult;
import com.kongur.monolith.weixin.service.AccessTokenService;
import com.kongur.monolith.weixin.service.ApiService;

/**
 * 微信 AccessToken 服务
 * <p>
 * 微信的AccessToken会定时实效，默认是7200秒(2个小时)，因此需要定时去刷新AccessToken
 * </p>
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("defaultAccessTokenService")
public class DefaultAccessTokenService implements AccessTokenService {

    private final Logger             log                = Logger.getLogger(getClass());

    /**
     * 主动调用微信平台接口时需要用到
     */
    private String                   accessToken;

    private ReentrantReadWriteLock   readWriteLock      = new ReentrantReadWriteLock();

    @Value("weixin.appId")
    private String                   appId;

    @Value("weixin.appSecret")
    private String                   appSecret;

    @Value("weixin.api.token.url.pattern")
    private String                   apiTokenUrlPattern = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    private String                   apiTokenUrl;

    @Resource(name = "defaultApiService")
    private ApiService               apiService;

    private ScheduledExecutorService executor;

    /**
     * 刷新时段，默认没3600秒刷新一次
     */
    private int                      refreshPeriod      = 3600;

    @PostConstruct
    public void init() {

        Assert.notNull(this.appId, "the appId can not be blank.");
        Assert.notNull(this.appSecret, "the appSecret can not be blank.");
        Assert.notNull(this.apiTokenUrlPattern, "the apiTokenPattern can not be blank.");

        this.apiTokenUrl = MessageFormat.format(this.apiTokenUrlPattern, this.appId, this.appSecret);

        if (log.isInfoEnabled()) {
            log.info("the appId->" + this.appId);
            log.info("the appSecret->" + this.appSecret);
            log.info("the api for access token url is->" + this.apiTokenUrl);
        }

        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor();
        }

        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                try {
                    refresh();
                } catch (Exception e) {
                    log.error("refresh access token error, apiTokenUrl=" + apiTokenUrl, e);
                }
            }
        }, 5, this.refreshPeriod, TimeUnit.SECONDS);
    }

    /**
     * 刷新AccessToken
     */
    private void refresh() {

        ServiceResult<String> result = apiService.executeGet(apiTokenUrl);
        JSONObject jsonObj = JSONObject.fromObject(result.getResult());

        if (jsonObj.containsKey("access_token")) {
            String accessToken = jsonObj.getString("access_token");
            WriteLock lock = this.readWriteLock.writeLock();
            lock.lock();

            try {
                this.accessToken = accessToken;
            } finally {
                lock.unlock();
            }

        } else {
            log.error("refresh access token error, response=" + result.getResult());
        }

    }

    @Override
    public String getAccessToken() {

        String token = null;
        ReadLock lock = readWriteLock.readLock();
        lock.lock();

        try {
            token = this.accessToken;
        } finally {
            lock.unlock();
        }

        return token;
    }

    @PreDestroy
    public void destroy() {
        if (this.executor != null) {
            this.executor.shutdown();
        }
    }

    public static void main(String[] args) {
        // String obj = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200}";
        // JSONObject jsonObj = JSONObject.fromObject(obj);
        //
        // System.out.println(jsonObj.entrySet());
        //
        //
        // Map<String, String> map = new HashMap<String, String>();
        // jsonObj.putAll(map);
        //
        // System.out.println(map);

        String appId = "wxe58afcd99f7a997e";
        String appSecret = "5dcf8eac1e99e983fc58e42376ab0267";
        String apiTokenPattern = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
        String apiToken = MessageFormat.format(apiTokenPattern, appId, appSecret);
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(apiToken);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String jsonData = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (StringUtil.isBlank(jsonData)) {
                return;
            }

            JSONObject jsonObj = JSONObject.fromObject(jsonData);
            System.out.println(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
