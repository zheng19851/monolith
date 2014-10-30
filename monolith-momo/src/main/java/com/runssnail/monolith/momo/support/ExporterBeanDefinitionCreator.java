package com.runssnail.monolith.momo.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * BeanDefinition创建工厂
 * 
 * @author zhengwei
 */
public interface ExporterBeanDefinitionCreator {

    public BeanDefinitionHolder createBeanDefinitionHolder(ExporterMetadata exporterMetadata);
}
