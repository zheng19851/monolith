package com.runssnail.monolith.momo.support.xfire;

import org.codehaus.xfire.spring.remoting.XFireExporter;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.StringUtils;

import com.runssnail.monolith.momo.support.AbstractExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterMetadata;

/**
 * xfire
 * 
 * @author zhengwei
 */
public class XfireExporterBeanDefinitionCreator extends AbstractExporterBeanDefinitionCreator {

    private static final XfireExporterBeanDefinitionCreator INSTANCE = new XfireExporterBeanDefinitionCreator();

    @Override
    public BeanDefinitionHolder createBeanDefinitionHolder(ExporterMetadata exporterMetadata) {
        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(XFireExporter.class);
        bd.getPropertyValues().add("serviceBean", new RuntimeBeanReference(exporterMetadata.getServiceRef()));
        bd.getPropertyValues().add("serviceClass", exporterMetadata.getServiceInterface());
        bd.getPropertyValues().add("xfire", new RuntimeBeanReference("xfire"));
        bd.getPropertyValues().add("serviceFactory", new RuntimeBeanReference("xfire.serviceFactory"));

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
