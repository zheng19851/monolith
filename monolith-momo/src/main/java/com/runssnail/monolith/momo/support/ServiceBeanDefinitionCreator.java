package com.runssnail.monolith.momo.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;


public interface ServiceBeanDefinitionCreator {

    BeanDefinitionHolder createBeanDefinitionHolder(ServiceMetadata metadata);

}
