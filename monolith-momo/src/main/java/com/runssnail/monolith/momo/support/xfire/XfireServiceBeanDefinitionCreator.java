package com.runssnail.monolith.momo.support.xfire;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.runssnail.monolith.momo.support.AbstractServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.DefaultServiceMetadata;
import com.runssnail.monolith.momo.support.ServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ServiceMetadata;

/**
 * xfire 客户端BeanDefinition创建工厂
 * 
 * @author zhengwei
 */
public class XfireServiceBeanDefinitionCreator extends AbstractServiceBeanDefinitionCreator {

    private static final XfireServiceBeanDefinitionCreator INSTANCE = new XfireServiceBeanDefinitionCreator();

    @Override
    public BeanDefinitionHolder createBeanDefinitionHolder(ServiceMetadata metadata) {
        if (!(metadata instanceof DefaultServiceMetadata)) {
            throw new RuntimeException("the ServiceMetadata must be 'DefaultServiceMetadata'");
        }

        DefaultServiceMetadata serviceMetadata = (DefaultServiceMetadata) metadata;

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(XFireClientFactoryBean.class);
        bd.getPropertyValues().add("wsdlDocumentUrl", serviceMetadata.getServiceUrl());
        bd.getPropertyValues().add("serviceClass", serviceMetadata.getServiceInterface());
        // bd.getPropertyValues().add("connectTimeout", connectTimeout);
        // bd.getPropertyValues().add("readTimeout", hessianServiceMetadata.getReadTimeout());

        String serviceId = buildServiceId(serviceMetadata.getId(), serviceMetadata.getServiceInterface());
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bd, serviceId);
        return holder;
    }

    public static ServiceBeanDefinitionCreator getInstance() {
        return INSTANCE;
    }

}
