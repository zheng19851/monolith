package com.runssnail.monolith.momo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.runssnail.monolith.momo.support.ServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ServiceBeanDefinitionFactory;
import com.runssnail.monolith.momo.support.ServiceMetadata;
import com.runssnail.monolith.momo.support.ServiceMetadataParser;
import com.runssnail.monolith.momo.support.ServiceMetadataParserFactory;

/**
 * 解析使用服务xml配置
 * 
 * @author zhengwei
 */
public class MomoServiceBeanDefinitionParser extends AbstractMomoBeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();

        String protocol = element.getAttribute("protocol");
        ServiceMetadataParser parser = ServiceMetadataParserFactory.getServiceMetadataParser(protocol);
        ServiceMetadata metadata = parser.parse(element, parserContext);

        ServiceBeanDefinitionCreator serviceBeanDefinitionCreator = ServiceBeanDefinitionFactory.getServiceBeanDefinitionCreator(protocol);

        BeanDefinitionHolder holder = serviceBeanDefinitionCreator.createBeanDefinitionHolder(metadata);

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        return holder.getBeanDefinition();
    }

}
