package com.runssnail.monolith.momo.support.httpinvoker;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.runssnail.monolith.momo.support.AbstractServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.DefaultServiceMetadata;
import com.runssnail.monolith.momo.support.ServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ServiceMetadata;

/**
 * xfire 客户端BeanDefinition创建工厂
 * 
 * @author zhengwei
 */
public class HttpInvokerServiceBeanDefinitionCreator extends AbstractServiceBeanDefinitionCreator {

    private static final HttpInvokerServiceBeanDefinitionCreator INSTANCE = new HttpInvokerServiceBeanDefinitionCreator();

    @Override
    public BeanDefinitionHolder createBeanDefinitionHolder(ServiceMetadata metadata) {
        if (!(metadata instanceof DefaultServiceMetadata)) {
            throw new RuntimeException("the ServiceMetadata must be 'DefaultServiceMetadata'");
        }

        DefaultServiceMetadata serviceMetadata = (DefaultServiceMetadata) metadata;

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(HttpInvokerProxyFactoryBean.class);
        bd.getPropertyValues().add("serviceUrl", serviceMetadata.getServiceUrl());
        bd.getPropertyValues().add("serviceInterface", serviceMetadata.getServiceInterface());
        // bd.getPropertyValues().add("connectTimeout", connectTimeout);
        // bd.getPropertyValues().add("readTimeout", serviceMetadata.getReadTimeout());

        String serviceId = buildServiceId(serviceMetadata.getId(), serviceMetadata.getServiceInterface());
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bd, serviceId);
        return holder;
    }

    public static ServiceBeanDefinitionCreator getInstance() {
        return INSTANCE;
    }

}
