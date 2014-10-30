package com.runssnail.monolith.momo.support.hessian;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.runssnail.monolith.momo.support.AbstractServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.ServiceMetadata;

/**
 * hessian 客户端BeanDefinition创建工厂
 * 
 * @author zhengwei
 */
public class HessianServiceBeanDefinitionCreator extends AbstractServiceBeanDefinitionCreator {

    private static final HessianServiceBeanDefinitionCreator INSTANCE = new HessianServiceBeanDefinitionCreator();

    @Override
    public BeanDefinitionHolder createBeanDefinitionHolder(ServiceMetadata metadata) {
        if (!(metadata instanceof HessianServiceMetadata)) {
            throw new RuntimeException("the ServiceMetadata must be 'HessianServiceMetadata'");
        }

        HessianServiceMetadata hessianServiceMetadata = (HessianServiceMetadata) metadata;

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setBeanClass(HessianProxyFactoryBean.class);
        bd.getPropertyValues().add("serviceUrl", hessianServiceMetadata.getServiceUrl());
        bd.getPropertyValues().add("serviceInterface", hessianServiceMetadata.getServiceInterface());
        // bd.getPropertyValues().add("connectTimeout", connectTimeout);
        bd.getPropertyValues().add("readTimeout", hessianServiceMetadata.getReadTimeout());

        String serviceId = buildServiceId(hessianServiceMetadata.getId(), hessianServiceMetadata.getServiceInterface());
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bd, serviceId);
        return holder;
    }

    public static ServiceBeanDefinitionCreator getInstance() {
        return INSTANCE;
    }

}
