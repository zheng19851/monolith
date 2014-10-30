package com.runssnail.monolith.momo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.BeanDefinitionParser;

/**
 * @author zhengwei
 */
public abstract class AbstractMomoBeanDefinitionParser implements BeanDefinitionParser {

    protected final Logger log = Logger.getLogger(getClass());

}
