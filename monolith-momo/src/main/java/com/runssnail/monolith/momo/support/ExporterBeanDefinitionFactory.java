package com.runssnail.monolith.momo.support;

import com.runssnail.monolith.momo.support.hessian.HessianExporterBeanDefinitionCreator;


public class ExporterBeanDefinitionFactory {

    public static ExporterBeanDefinitionCreator getExporterBeanDefinitionCreator(String protocol) {
        ExporterBeanDefinitionCreator creator = null;
        if(EnumProtocol.isHessian(protocol)) {
            creator = HessianExporterBeanDefinitionCreator.getInstance();
        }
        return creator;
    }

    
    
}
