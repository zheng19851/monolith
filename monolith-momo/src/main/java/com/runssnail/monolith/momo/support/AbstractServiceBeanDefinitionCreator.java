package com.runssnail.monolith.momo.support;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public abstract class AbstractServiceBeanDefinitionCreator implements ServiceBeanDefinitionCreator {

    protected final Logger log = Logger.getLogger(getClass());

    /**
     * 生成服务id
     * 
     * @param id 配置的id
     * @param serviceInterface 配置的服务接口
     * @return
     */
    protected String buildServiceId(String id, String serviceInterface) {
        String serviceId = null;
        if (StringUtils.hasText(id)) {
            serviceId = id;
        } else {
            serviceId = serviceInterface;
        }

        if (log.isDebugEnabled()) {
            log.debug("build service id success, serviceId=" + serviceId);
        }
        return serviceId;
    }
}
