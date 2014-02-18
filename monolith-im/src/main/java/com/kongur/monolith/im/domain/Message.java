package com.kongur.monolith.im.domain;

import java.util.Map;

/**
 * 接收到的消息
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface Message {

    Message NULL_MESSAGE = new Message() {

                             @Override
                             public String getToUserName() {
                                 return null;
                             }

                             @Override
                             public String getTimestamp() {
                                 return null;
                             }

                             @Override
                             public String getString(String key) {
                                 return null;
                             }

                             @Override
                             public String getSignature() {
                                 return null;
                             }

                             @Override
                             public Map<String, Object> getParams() {
                                 return null;
                             }

                             @Override
                             public Object getParam(String key) {
                                 return null;
                             }

                             @Override
                             public String getNonce() {
                                 return null;
                             }

                             @Override
                             public String getMsgType() {
                                 return null;
                             }

                             @Override
                             public String getMsgId() {
                                 return null;
                             }

                             @Override
                             public String getFromUserName() {
                                 return null;
                             }

                             @Override
                             public long getCreateTime() {
                                 return -1;
                             }

                             @Override
                             public boolean containsKey(String key) {
                                 return false;
                             }
                         };

    /**
     * 消息ID
     * 
     * @return
     */
    String getMsgId();

    /**
     * 返回消息类型
     * 
     * @return
     */
    String getMsgType();

    /**
     * 加密签名
     * 
     * @return
     */
    String getSignature();

    /**
     * 时间戳
     * 
     * @return
     */
    String getTimestamp();

    /**
     * 随机数
     * 
     * @return
     */
    String getNonce();

    /**
     * 发消息方
     * 
     * @return
     */
    String getFromUserName();

    /**
     * 获取收消息方
     * 
     * @return
     */
    String getToUserName();

    /**
     * 创建时间
     * 
     * @return
     */
    long getCreateTime();

    /**
     * 消息业务数据
     * 
     * @return
     */
    Map<String, Object> getParams();

    /**
     * 获取业务数据
     * 
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * 获取业务数据
     * 
     * @param key
     * @return
     */
    Object getParam(String key);

    /**
     * 是否包含这个参数
     * 
     * @param key
     * @return
     */
    boolean containsKey(String key);

}
