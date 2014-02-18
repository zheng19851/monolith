package com.kongur.monolith.im.weixin.service;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.serivce.AccessTokenService;
import com.kongur.monolith.im.serivce.ApiService;

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
     * 主动调用微信平台接口时需要用到(基础接口用)
     */
    private String                   accessToken;

    private ReentrantReadWriteLock   readWriteLock      = new ReentrantReadWriteLock();

    @Value("${weixin.appId}")
    private String                   appId;

    @Value("${weixin.appSecret}")
    private String                   appSecret;

    @Value("${weixin.api.token.url.pattern}")
    private String                   apiTokenUrlPattern = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    private String                   apiTokenUrl;

    @Resource(name = "defaultApiService")
    private ApiService               apiService;

    private ScheduledExecutorService executor;

    /**
     * 刷新时段，默认没5400秒(一个半小时)刷新一次
     */
    private int                      refreshPeriod      = 5400;

    @Value("${weixin.api.token.refresh.disable}")
    private boolean                  disableRefresh     = false;

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

        if (!disableRefresh) {
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

    }

    /**
     * 刷新AccessToken
     */
    public ServiceResult<String> refresh() {

        final ServiceResult<String> result = new ServiceResult<String>();

        ServiceResult<JSONObject> jsonResult = apiService.doGet(apiTokenUrl);

        if (!jsonResult.isSuccess()) {
            log.error("refresh access token error, apiTokenUrl=" + apiTokenUrl + ", errorCode="
                      + jsonResult.getResultCode() + ", errorInfo=" + jsonResult.getResultInfo());

            result.setError(jsonResult.getResultCode(), jsonResult.getResultInfo());
            return result;
        }

        final JSONObject jsonObj = jsonResult.getResult();

        String accessToken = null;
        if (jsonObj.containsKey("access_token")) {
            accessToken = jsonObj.getString("access_token");
            WriteLock lock = this.readWriteLock.writeLock();
            lock.lock();

            try {
                this.accessToken = accessToken;
            } finally {
                lock.unlock();
            }

            if (log.isDebugEnabled()) {
                log.debug("refresh access token successfully, new AccessToken=" + accessToken);
            }

        } else {
            log.error("refresh access token error, response=" + jsonResult.getResult());
            result.setError("2001", "can not find access token.");
            return result;
        }

        result.setResult(accessToken);
        result.setSuccess(true);
        return result;

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

}
