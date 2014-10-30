package com.runssnail.monolith.momo.support;

/**
 * 客户端元数据描述
 *
 * @author zhengwei
 */
public class CommonServiceMetadata extends AbstractMetadata implements ServiceMetadata {

    // private String connectTimeout ;

    private String readTimeout;

    private String host;

    private String port;

    private String contextPath;

    private String name;

    private String serviceUrl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(String readTimeout) {
        this.readTimeout = readTimeout;
    }

}
