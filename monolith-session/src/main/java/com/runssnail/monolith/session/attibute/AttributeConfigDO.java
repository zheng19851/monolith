package com.runssnail.monolith.session.attibute;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.runssnail.monolith.session.store.CookieSessionStore;

import java.io.Serializable;

/**
 * cookie������Ϣ
 * 
 * @author zhengwei
 * @date��2011-6-15
 */

public class AttributeConfigDO implements Serializable {

    private static final long serialVersionUID = 3190254383299004162L;

    /**
     * ������ʹ�õ����Ե����֣���Ӧ��HttpSession.getAttribute()�����Ĳ���
     */
    private String  key;

    /**
     * ���浽�ͻ���ʱʹ�õ�����
     */
    private String  clientKey;

    /**
     * �Ƿ���Ҫ����
     */
    private boolean encrypt    = true;

    /**
     * ���������͵�store�洢, Ĭ����CookieSessionAttributeStore
     */
    private String  storeKey   = CookieSessionStore.class.getSimpleName();

    /**
     * �洢���������ڣ���ʧЧʱ��
     */
    private int     lifeCycle  = -1;

    /**
     * cookie������
     */
    private String  domain     = "localhost";

    /**
     * cookie��·��
     */
    private String  cookiePath = "/";

    /**
     * cookie��httpOnly����
     */
    private boolean httpOnly;

    /**
     * �Ƿ���ֻ�����ԣ�һ��ֻ����sessionID
     */
    private boolean readOnly;

    public AttributeConfigDO() {

    }

    public AttributeConfigDO(String key, String clientKey, boolean encrypt, String storeKey) {
        this.key = key;
        this.clientKey = clientKey;
        this.encrypt = encrypt;
        this.storeKey = storeKey;
    }

    public AttributeConfigDO(String key, String clientKey, boolean encrypt, String storeKey, int lifeCycle) {
        this.key = key;
        this.clientKey = clientKey;
        this.encrypt = encrypt;
        this.storeKey = storeKey;
        this.lifeCycle = lifeCycle;
    }

    public String getStoreKey() {
        return storeKey;
    }

    public void setStoreKey(String storeKey) {
        this.storeKey = storeKey;
    }

    /**
     * �ڲ�key
     * 
     * @return
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * �����ڿͻ��˵�����
     * 
     * @return
     */
    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    /**
     * �Ƿ����
     * 
     * @return
     */
    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public int getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(int lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
