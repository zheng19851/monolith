package com.runssnail.monolith.momo;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Test2 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context-test2.xml");

        TestService testService = (TestService) context.getBean("testService");
        testService.print();

        System.out.println(context.getBean("/remoting/httpInvoker/1.0/com/runssnail/monolith/momo/TestService.htm"));

        TestService remoteTestService = (TestService) context.getBean("com.runssnail.monolith.momo.TestService");

//        remoteTestService.print();
        System.out.println(remoteTestService);

    }

}
