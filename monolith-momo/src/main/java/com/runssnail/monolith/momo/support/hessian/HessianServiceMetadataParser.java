package com.runssnail.monolith.momo.support.hessian;

import java.util.List;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.runssnail.monolith.momo.support.AbstractServiceMetadataParser;
import com.runssnail.monolith.momo.support.ServiceMetadata;
import com.runssnail.monolith.momo.support.ServiceMetadataParser;

/**
 * hessian 客户端元数据解析器
 * 
 * @author zhengwei
 */
public class HessianServiceMetadataParser extends AbstractServiceMetadataParser {

    private static final HessianServiceMetadataParser INSTANCE      = new HessianServiceMetadataParser();

    public static final String                        HTTP_PROTOCOL = "http://";

    private static final String                       SERVICE_URL   = "serviceUrl";

    @Override
    public ServiceMetadata parse(Element element, ParserContext parserContext) {

        String id = element.getAttribute("id");

        String protocol = element.getAttribute("protocol");

        String serviceInterface = element.getAttribute("serviceInterface");

        // String connectTimeout = element.getAttribute("connectTimeout");

        String readTimeout = element.getAttribute("readTimeout");

        List<Element> childElts = DomUtils.getChildElements(element);

        String serviceUrl = getServiceUrl(parserContext, childElts);

        String host = element.getAttribute("host");

        String port = element.getAttribute("port");

        String contextPath = element.getAttribute("contextPath");

        String version = element.getAttribute("version");

        String name = element.getAttribute("name");

        String serviceName = buildServiceName(name, protocol, version, serviceInterface);

        if (!StringUtils.hasText(serviceUrl)) {
            serviceUrl = buildServiceUrl(host, port, contextPath, serviceName);
        }

        HessianServiceMetadata metadata = new HessianServiceMetadata();
        metadata.setId(id);
        metadata.setContextPath(contextPath);
        metadata.setHost(host);
        metadata.setName(name);
        metadata.setPort(port);
        metadata.setProtocol(protocol);
        metadata.setReadTimeout(readTimeout);
        metadata.setServiceInterface(serviceInterface);
        metadata.setServiceName(serviceName);
        metadata.setServiceUrl(serviceUrl);
        metadata.setVersion(version);
        
        return metadata;
    }

    private String getServiceUrl(ParserContext parserContext, List<Element> childElts) {
        Element serviceUrlEle = getServiceUrlEle(parserContext, childElts);
        if (serviceUrlEle != null) {
            String serviceUrl = serviceUrlEle.getAttribute("value");
            return serviceUrl;
        }
        return null;
    }

    private Element getServiceUrlEle(ParserContext parserContext, List<Element> childElts) {
        for (Element elt : childElts) {
            String localName = parserContext.getDelegate().getLocalName(elt);

            if (SERVICE_URL.equals(localName)) {
                return elt;
            }
        }

        return null;
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

    public static ServiceMetadataParser getInstance() {
        return INSTANCE;
    }

}
