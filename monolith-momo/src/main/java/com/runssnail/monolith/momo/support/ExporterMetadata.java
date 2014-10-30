package com.runssnail.monolith.momo.support;

/**
 * 服务发布者元数据描述
 * 
 * @author zhengwei
 */
public interface ExporterMetadata extends Metadata {

    /**
     * 服务引用
     * 
     * @return
     */
    String getServiceRef();
}
