package com.runssnail.monolith.momo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 解析使用服务xml配置
 * 
 * @author zhengwei
 */
public class MomoServiceBeanDefinitionParser extends AbstractMomoBeanDefinitionParser {

    public static final String HTTP_PROTOCOL = "http://";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();

        String host = element.getAttribute("host");

        String port = element.getAttribute("port");

        String contextPath = element.getAttribute("contextPath");

        String id = element.getAttribute("id");
        
        String name = element.getAttribute("name");

        String protocol = element.getAttribute("protocol");

        String version = element.getAttribute("version");

        String serviceInterface = element.getAttribute("serviceInterface");

        String serviceName = buildServiceName(name, protocol, version, serviceInterface);

        String serviceUrl = buildServiceUrl(host, port, contextPath, serviceName);

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(HessianProxyFactoryBean.class);
        bd.getPropertyValues().add("serviceUrl", serviceUrl);
        bd.getPropertyValues().add("serviceInterface", serviceInterface);

        String serviceId = buildServiceId(id, serviceInterface);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bd, serviceId);

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        return bd;
    }

    private String buildServiceUrl(String host, String port, String contextPath, String serviceName) {

        StringBuilder sb = new StringBuilder(HTTP_PROTOCOL);
        sb.append(host).append(":").append(port);
        if (StringUtils.hasText(contextPath) && !contextPath.equals("/")) {
            sb.append(contextPath);
        }

        sb.append(serviceName);

        String serviceUrl = sb.toString();
        if (log.isDebugEnabled()) {
            log.debug("build service url success, serviceUrl=" + serviceUrl);
        }

        return serviceUrl;
    }

}
