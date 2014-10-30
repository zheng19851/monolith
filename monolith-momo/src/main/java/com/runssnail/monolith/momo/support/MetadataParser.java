package com.runssnail.monolith.momo.support;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public abstract class MetadataParser {

    protected final Logger      log                         = Logger.getLogger(getClass());

    private static final String DEFAULT_SERVICE_NAME_PREFIX = "/remoting/";

    private static final String DEFAULT_SERVICE_NAME_SUFFIX = ".htm";

    /**
     * 生成服务名称
     * 
     * @param name 服务名称
     * @param protocol 协议, 默认hessian
     * @param version 版本, 默认1.0
     * @param serviceInterface 服务接口
     * @return
     */
    protected String buildServiceName(String name, String protocol, String version, String serviceInterface) {

        StringBuilder sb = new StringBuilder(DEFAULT_SERVICE_NAME_PREFIX + protocol + "/" + version + "/");
        if (StringUtils.hasText(name)) {
            sb.append(name);
        } else {

            serviceInterface = serviceInterface.replaceAll("\\.", "/");
            sb.append(serviceInterface);
        }

        String serviceName = sb.toString();

        if (!serviceName.endsWith(DEFAULT_SERVICE_NAME_SUFFIX)) {
            serviceName = serviceName + DEFAULT_SERVICE_NAME_SUFFIX;
        }

        if (log.isDebugEnabled()) {
            log.debug("build service name success, serviceName=" + serviceName);
        }

        return serviceName;
    }
}
