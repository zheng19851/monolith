package com.kongur.monolith.dal.test.conf;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kongur.monolith.dal.route.conf.DefaultRouteRulesManager;


/**
 * 默认路由规则管理器实现
 * 
 * @author zhengwei
 */
public class DefaultRouteRulesManagerTest {
    
    public static void main(String[] args) throws Exception {
//        DefaultRouteRulesManager routeRulesManager = new DefaultRouteRulesManager();
//        Resource resouce = new ClassPathResource("rules.xml");
//        routeRulesManager.setConfigLocation(resouce);
//        routeRulesManager.afterPropertiesSet();
//        
//        
//        System.out.println(routeRulesManager.getRules());
        
        ApplicationContext context = new ClassPathXmlApplicationContext("data-source-beans.xml");

        DefaultRouteRulesManager defaultRouteRulesManager = (DefaultRouteRulesManager) context.getBean("defaultRouteRulesManager");

        System.out.println(defaultRouteRulesManager.getRules());
        
    }
}
