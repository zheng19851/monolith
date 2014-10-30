package com.runssnail.monolith.momo.support.hessian;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.util.StringUtils;

import com.runssnail.monolith.momo.support.AbstractExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ExporterMetadata;

/**
 * Hessian 服务BeanDefinitionHolder创建工厂
 * 
 * @author zhengwei
 */
public class HessianExporterBeanDefinitionCreator extends AbstractExporterBeanDefinitionCreator {

    private static final HessianExporterBeanDefinitionCreator INSTANCE = new HessianExporterBeanDefinitionCreator();

    @Override
    public BeanDefinitionHolder createBeanDefinitionHolder(ExporterMetadata exporterMetadata) {

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(HessianServiceExporter.class);
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
