package com.kongur.monolith.im.serivce;


/**
 * 
 * signature 验证器
 *
 *@author zhengwei
 *
 *@date 2014-2-14	
 *
 */
public interface SignatureValidator {

    /**
     * 验证签名
     *
     * @param signature 加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return
     */
    boolean validate(String signature, String timestamp, String nonce);
    
}

