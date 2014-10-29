package com.runssnail.monolith.momo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.StringUtils;

/**
 * @author zhengwei
 */
public abstract class AbstractMomoBeanDefinitionParser implements BeanDefinitionParser {

    protected final Logger     log                         = Logger.getLogger(getClass());

    public static final String DEFAULT_SERVICE_NAME_PREFIX = "/remoting/";

    public static final String DEFAULT_SERVICE_NAME_SUFFIX = ".htm";

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
            // String shortName = ClassUtils.getShortName(serviceInterface);
            // // 首字母小写
            // char frist = shortName.charAt(0);
            // StringBuilder stortNameBuilder = new StringBuilder(shortName);
            // stortNameBuilder.deleteCharAt(0).insert(0, Character.toLowerCase(frist));
            // sb.append(stortNameBuilder.toString());

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
