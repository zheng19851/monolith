package com.kongur.monolith.im.weixin.service;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kongur.monolith.im.serivce.SignatureValidator;
import com.kongur.monolith.lang.StringUtil;

/**
 * 微信签名验证器
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
@Service("signatureValidator")
public class DefaultSignatureValidator implements SignatureValidator {

    private final Logger log             = Logger.getLogger(getClass());

    /**
     * 微信token
     */
    @Value("${weixin.token}")
    private String       token;

    /**
     * 是否关闭验证, 默认为开启验证
     */
    @Value("${weixin.token.validate.disable}")
    private boolean      disableValidate = false;

    @PostConstruct
    public void init() {
        if (StringUtil.isBlank(this.token)) {
            throw new IllegalArgumentException("the token can not be blank.");
        }

        if (log.isDebugEnabled()) {
            log.debug("token=" + this.token + ", disableValidate=" + this.disableValidate);
        }

    }

    /**
     * 加密/校验流程如下：
     * <ul>
     * <li>1. 将token、timestamp、nonce三个参数进行字典序排序</li>
     * <li>2. 将三个参数字符串拼接成一个字符串进行sha1加密</li>
     * <li>3.开发者获得加密后的字符串可与signature对比，标识该请求来源于微信</li>
     * <ul>
     */
    @Override
    public boolean validate(String signature, String timestamp, String nonce) {

        if (disableValidate) {
            return true;
        }

        if (StringUtil.isBlank(signature) || StringUtil.isBlank(timestamp) || StringUtil.isBlank(nonce)) {
            log.error("validate signature error, the validate arguments can not be blank. signature=" + signature
                      + ", timestamp=" + timestamp + ", nonce=" + nonce);
            return false;
        }

        String[] signatureArray = { this.token, timestamp, nonce };

        Arrays.sort(signatureArray); // 自然排序

        // 加密前的签名
        String internalSignature = StringUtil.join(signatureArray);

        // 加密后的签名
        String encodedInternalSignature = DigestUtils.sha1Hex(internalSignature);

        return signature.equals(encodedInternalSignature);
    }

}
