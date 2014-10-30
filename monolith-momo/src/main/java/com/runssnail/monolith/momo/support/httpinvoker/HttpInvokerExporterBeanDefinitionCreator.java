package com.runssnail.monolith.momo.support.httpinvoker;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.util.StringUtils;

import com.runssnail.monolith.momo.support.AbstractExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterMetadata;

/**
 * xfire
 * 
 * @author zhengwei
 */
public class HttpInvokerExporterBeanDefinitionCreator extends AbstractExporterBeanDefinitionCreator {

    private static final HttpInvokerExporterBeanDefinitionCreator INSTANCE = new HttpInvokerExporterBeanDefinitionCreator();

    @Override
    public BeanDefinitionHolder createBeanDefinitionHolder(ExporterMetadata exporterMetadata) {
        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(HttpInvokerServiceExporter.class);
        bd.getPropertyValues().add("service", new RuntimeBeanReference(exporterMetadata.getServiceRef()));
        bd.getPropertyValues().add("serviceInterface", exporterMetadata.getServiceInterface());

        String[] aliases = null;
        if (StringUtils.hasText(exporterMetadata.getId())) {
            aliases = new String[] { exporterMetadata.getId() };
        }

        BeanDefinitionHolder holder = new BeanDefinitionHolder(bd, exporterMetadata.getServiceName(), aliases);

        return holder;
    }

    public static ExporterBeanDefinitionCreator getInstance() {
        return INSTANCE;
    }

}
