package com.runssnail.monolith.momo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.runssnail.monolith.momo.support.ExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterBeanDefinitionFactory;
import com.runssnail.monolith.momo.support.ExporterMetadata;
import com.runssnail.monolith.momo.support.ExporterMetadataParser;
import com.runssnail.monolith.momo.support.ExporterMetadataParserFactory;

/**
 * 解析发布服务xml配置
 * 
 * @author zhengwei
 */
public class MomoExporterBeanDefinitionParser extends AbstractMomoBeanDefinitionParser {

    private static final String DEFAULT_HANDLER_MAPPING = "org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        BeanDefinitionRegistry registry = parserContext.getRegistry();

        checkDefaultHandlerMapping(element, registry);

        String protocol = element.getAttribute("protocol");

        ExporterMetadataParser parser = ExporterMetadataParserFactory.getExporterMetadataParser(protocol);

        ExporterMetadata exporterMetadata = parser.parse(element, parserContext);

        ExporterBeanDefinitionCreator exporterBeanDefinitionCreator = ExporterBeanDefinitionFactory.getExporterBeanDefinitionCreator(protocol);

        BeanDefinitionHolder holder = exporterBeanDefinitionCreator.createBeanDefinitionHolder(exporterMetadata);

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        if (log.isDebugEnabled()) {
            log.debug("export service success, serviceName=" + exporterMetadata.getServiceName());
        }

        return holder.getBeanDefinition();
    }

    private void checkDefaultHandlerMapping(Element element, BeanDefinitionRegistry registry) {

        String orderStr = element.getAttribute("handlerMappingOrder");

        if (registry.containsBeanDefinition(DEFAULT_HANDLER_MAPPING)) {
            BeanDefinition handlerMappingBeanDefinition = registry.getBeanDefinition(DEFAULT_HANDLER_MAPPING);
            if (log.isDebugEnabled()) {
                log.debug("the '" + DEFAULT_HANDLER_MAPPING + "' has exists");
            }
            handlerMappingBeanDefinition.getPropertyValues().add("order", orderStr);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("can not find '" + DEFAULT_HANDLER_MAPPING + "'");
            }
            synchronized (DEFAULT_HANDLER_MAPPING) {
                if (!registry.containsBeanDefinition(DEFAULT_HANDLER_MAPPING)) {
                    BeanDefinition handlerMappingBeanDefinition = new RootBeanDefinition();
                    handlerMappingBeanDefinition.setBeanClassName(DEFAULT_HANDLER_MAPPING);
                    handlerMappingBeanDefinition.getPropertyValues().add("order", orderStr);

                    registry.registerBeanDefinition(DEFAULT_HANDLER_MAPPING, handlerMappingBeanDefinition);
                    if (log.isDebugEnabled()) {
                        log.debug("register '" + DEFAULT_HANDLER_MAPPING + "' success");
                    }
                }
            }

        }

    }

}
