package com.runssnail.monolith.momo.support;

import com.runssnail.monolith.momo.support.hessian.HessianServiceBeanDefinitionCreator;

public class ServiceBeanDefinitionFactory {

    public static ServiceBeanDefinitionCreator getServiceBeanDefinitionCreator(String protocol) {
        ServiceBeanDefinitionCreator creator = null;
        if (EnumProtocol.isHessian(protocol)) {
            creator = HessianServiceBeanDefinitionCreator.getInstance();
        }
        return creator;
    }

}
