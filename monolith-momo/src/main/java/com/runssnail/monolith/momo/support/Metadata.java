package com.runssnail.monolith.momo.support;

/**
 * 配置元数据
 * 
 * @author zhengwei
 *
 */
public interface Metadata {

    /**
     * 服务id
     * 
     * @return
     */
    String getId();

    /**
     * 服务接口
     * 
     * @return
     */
    String getServiceInterface();

    /**
     * 服务名称
     * 
     * @return
     */
    String getServiceName();

    /**
     * 协议
     * 
     * @return
     */
    String getProtocol();

    /**
     * 服务版本
     * 
     * @return
     */
    String getVersion();

}
