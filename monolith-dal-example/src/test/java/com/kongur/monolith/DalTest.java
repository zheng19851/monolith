package com.kongur.monolith;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kongur.monolith.dao.SystemUserDAO;

/**
 * @author zhengwei
 */
public class DalTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        SystemUserDAO systemUserDAO = (SystemUserDAO) context.getBean("systemUserDAO");

        System.out.println(systemUserDAO.selectSystemUsers());

    }

}
