package com.runssnail.monolith.momo.support;

import com.runssnail.monolith.momo.support.hessian.HessianExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.httpinvoker.HttpInvokerExporterBeanDefinitionCreator;
import com.runssnail.monolith.momo.support.xfire.XfireExporterBeanDefinitionCreator;

public class ExporterBeanDefinitionFactory {

    public static ExporterBeanDefinitionCreator getExporterBeanDefinitionCreator(String protocol) {
        ExporterBeanDefinitionCreator creator = null;
        if (EnumProtocol.isHessian(protocol)) {
            creator = HessianExporterBeanDefinitionCreator.getInstance();
        } else if (EnumProtocol.isXfire(protocol)) {
            creator = XfireExporterBeanDefinitionCreator.getInstance();
        } else if (EnumProtocol.isHttpInvoker(protocol)) {
            creator = HttpInvokerExporterBeanDefinitionCreator.getInstance();
        } else {
            throw new RuntimeException("can not find 'ExporterBeanDefinitionCreator', protocol=" + protocol);
        }
        return creator;
    }

}
