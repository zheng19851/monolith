package com.runssnail.monolith.momo.support;

import com.runssnail.monolith.momo.support.hessian.HessianServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.httpinvoker.HttpInvokerServiceBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.xfire.XfireServiceBeanDefinitionCreator;

public class ServiceBeanDefinitionFactory {

    public static ServiceBeanDefinitionCreator getServiceBeanDefinitionCreator(String protocol) {
        ServiceBeanDefinitionCreator creator = null;
        if (EnumProtocol.isHessian(protocol)) {
            creator = HessianServiceBeanDefinitionCreator.getInstance();
        } else if (EnumProtocol.isXfire(protocol)) {
            creator = XfireServiceBeanDefinitionCreator.getInstance();
        } else if (EnumProtocol.isHttpInvoker(protocol)) {
            creator = HttpInvokerServiceBeanDefinitionCreator.getInstance();
        } else {
            throw new RuntimeException("can not find 'ServiceBeanDefinitionCreator', protocol=" + protocol);
        }
        return creator;
    }

}
