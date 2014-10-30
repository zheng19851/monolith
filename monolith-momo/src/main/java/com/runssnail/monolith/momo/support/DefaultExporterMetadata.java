package com.runssnail.monolith.momo.support;

/**
 * 服务发布者元数据描述
 * 
 * @author zhengwei
 */
public class DefaultExporterMetadata extends AbstractMetadata implements ExporterMetadata {

    /**
     * 服务引用
     */
    private String serviceRef;

    private String name;

    public String getServiceRef() {
        return serviceRef;
    }

    public void setServiceRef(String serviceRef) {
        this.serviceRef = serviceRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
