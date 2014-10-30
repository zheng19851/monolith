package com.runssnail.monolith.momo.support;

import java.util.List;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class AbstractExporterMetadataParser extends MetadataParser implements ExporterMetadataParser {

    private static final String SERVICE_NAME = "serviceName";

    protected String getServiceName(ParserContext parserContext, List<Element> childElts) {
        Element serviceNameEle = getServiceNameEle(parserContext, childElts);
        if (serviceNameEle != null) {
            String serviceUrl = serviceNameEle.getAttribute("value");
            return serviceUrl;
        }
        return null;
    }

    private Element getServiceNameEle(ParserContext parserContext, List<Element> childElts) {
        for (Element elt : childElts) {
            String localName = parserContext.getDelegate().getLocalName(elt);

            if (SERVICE_NAME.equals(localName)) {
                return elt;
            }
        }

        return null;
    }

}
