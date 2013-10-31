package com.kongur.monolith.session.attibute;

/**
 * cookie配置信息
 * 
 * @author zhengwei
 * @date：2011-6-15
 */

public class AttributeConfigDO {

    /**
     * 程序中使用的属性的名字，对应于HttpSession.getAttribute()方法的参数
     */
    private String  key;

    /**
     * 保存时使用的名字，譬如cookie的名字
     */
    private String  clientKey;

    /**
     * 是否需要加密
     */
    private boolean encrypt    = true;

    /**
     * 用哪种类型的store存储
     */
    private String  storeKey;

    /**
     * 存储的生命周期，即失效时间
     */
    private int     lifeCycle  = -1;

    /**
     * cookie的域名
     */
    private String  domain     = "localhost";

    /**
     * cookie的路径
     */
    private String  cookiePath = "/";

    /**
     * cookie的httpOnly属性
     */
    private boolean httpOnly;

    /**
     * 是否是只读属性，一般只用于sessionID
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
     * 内部key
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
     * 保存在客户端的名称
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
     * 是否加密
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

}
