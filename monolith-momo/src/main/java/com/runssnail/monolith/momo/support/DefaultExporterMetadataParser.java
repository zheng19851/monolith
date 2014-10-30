package com.runssnail.monolith.momo.support;

import java.util.List;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * 默认服务端的元数据解析器
 * 
 * @author zhengwei
 */
public class DefaultExporterMetadataParser extends AbstractExporterMetadataParser {

    private static final DefaultExporterMetadataParser INSTANCE = new DefaultExporterMetadataParser();

    @Override
    public ExporterMetadata parse(Element element, ParserContext parserContext) {

        List<Element> childElts = DomUtils.getChildElements(element);

        String serviceName = getServiceName(parserContext, childElts);

        String serviceRef = element.getAttribute("serviceRef");

        String serviceInterface = element.getAttribute("serviceInterface");

        String id = element.getAttribute("id");

        String protocol = element.getAttribute("protocol");
        String version = element.getAttribute("version");

        // 服务简单名称
        String name = element.getAttribute("name");

        if (!StringUtils.hasText(serviceName)) {

            serviceName = buildServiceName(name, protocol, version, serviceInterface);
        }

        DefaultExporterMetadata metadata = createExporterMetadata();

        metadata.setId(id);
        metadata.setProtocol(protocol);
        metadata.setServiceInterface(serviceInterface);
        metadata.setServiceName(serviceName);
        metadata.setServiceRef(serviceRef);
        metadata.setVersion(version);
        metadata.setName(name);

        return metadata;
    }

    protected DefaultExporterMetadata createExporterMetadata() {
        return new DefaultExporterMetadata();
    }

    public static ExporterMetadataParser getInstance() {
        return INSTANCE;
    }

}
