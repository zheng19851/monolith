package com.kongur.monolith.dal.test.conf;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kongur.monolith.dal.test.dao.UserDAO;

public class DefaultDataSourceRouterTest {

    public static void main(String[] args) throws Exception {
        // DefaultRouteRulesManager routeRulesManager = new DefaultRouteRulesManager();
        // Resource resouce = new ClassPathResource("rules.xml");
        // routeRulesManager.setConfigLocation(resouce);
        // routeRulesManager.afterPropertiesSet();
        //
        // List<Rule> rules = routeRulesManager.getRules();
        // System.out.println(rules);
        //
        // String statementId = "wx_message.selectMessage";
        // for (Rule rule : rules) {
        // if(rule.isMatch(statementId, new Object())) {
        // System.out.println("matched, rule=" + rule);
        // break;
        // }
        // }
        //

        ApplicationContext context = new ClassPathXmlApplicationContext("data-source-beans.xml", "dao-beans.xml");

        // DataSourceRouter dataSourceRouter = (DataSourceRouter) context.getBean("router");
        //
        // UserDO message = new UserDO();
        // message.setFromUser("1233847333");
        // message.setId(8L);
        //
        // System.out.println(dataSourceRouter.routeDataSource("wx_message.selectMessageff", message));

        UserDAO userDAO = (UserDAO) context.getBean("userDAO");

        System.out.println(userDAO.selectById(4));

    }

}
