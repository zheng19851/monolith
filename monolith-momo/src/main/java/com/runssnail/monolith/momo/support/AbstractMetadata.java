package com.runssnail.monolith.momo.support;

/**
 * 共用的元数据
 * 
 * @author zhengwei
 */
public abstract class AbstractMetadata implements Metadata {

    /**
     * 服务id
     */
    private String id;

    /**
     * 服务接口
     */
    private String serviceInterface;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 版本
     */
    private String version;

    /**
     * 服务名称
     */
    private String serviceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
