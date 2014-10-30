package com.runssnail.monolith.momo.support;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 客户端元数据解析器
 * 
 * @author zhengwei
 *
 */
public interface ServiceMetadataParser {

    ServiceMetadata parse(Element element, ParserContext parserContext);
    
}
