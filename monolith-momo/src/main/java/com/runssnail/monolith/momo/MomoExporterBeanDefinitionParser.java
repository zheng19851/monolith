package com.runssnail.monolith.momo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 解析发布服务xml配置
 * 
 * @author zhengwei
 */
public class MomoExporterBeanDefinitionParser extends AbstractMomoBeanDefinitionParser {

    private static final String DEFAULT_HANDLER_MAPPING = "org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String orderStr = element.getAttribute("handlerMappingOrder");

        BeanDefinitionRegistry registry = parserContext.getRegistry();

        if (registry.containsBeanDefinition(DEFAULT_HANDLER_MAPPING)) {
            BeanDefinition handlerMappingBeanDefinition = registry.getBeanDefinition(DEFAULT_HANDLER_MAPPING);
            log.warn("the '" + DEFAULT_HANDLER_MAPPING + "' has exists");
            handlerMappingBeanDefinition.getPropertyValues().add("order", orderStr);
        } else {
            log.warn("can not find '" + DEFAULT_HANDLER_MAPPING + "'");
            synchronized (DEFAULT_HANDLER_MAPPING) {
                if (!registry.containsBeanDefinition(DEFAULT_HANDLER_MAPPING)) {
                    BeanDefinition handlerMappingBeanDefinition = new RootBeanDefinition();
                    handlerMappingBeanDefinition.setBeanClassName(DEFAULT_HANDLER_MAPPING);
                    handlerMappingBeanDefinition.getPropertyValues().add("order", orderStr);

                    registry.registerBeanDefinition(DEFAULT_HANDLER_MAPPING, handlerMappingBeanDefinition);
                    log.warn("register '" + DEFAULT_HANDLER_MAPPING + "' success");
                }
            }

        }

        String service = element.getAttribute("serviceRef");

        String serviceInterface = element.getAttribute("serviceInterface");

        String protocol = element.getAttribute("protocol");

        String version = element.getAttribute("version");

        String id = element.getAttribute("id");

        // 服务名称
        String name = element.getAttribute("name");

        String serviceName = buildServiceName(name, protocol, version, serviceInterface);

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(HessianServiceExporter.class);
        bd.getPropertyValues().add("service", new RuntimeBeanReference(service));
        bd.getPropertyValues().add("serviceInterface", serviceInterface);

        String[] aliases = null;
        if (StringUtils.hasText(id)) {
            aliases = new String[] { id };
        }
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bd, serviceName, aliases);

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        return bd;
    }
}
