package com.kongur.monolith;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


public class TransactionTemplateTest {
    
    
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        TransactionTemplate transactionTemplate = (TransactionTemplate) context.getBean("transactionTemplate");
        
        transactionTemplate.execute(new TransactionCallback() {
            
            @Override
            public Object doInTransaction(TransactionStatus status) {
                
                
                return null;
            }
        });

    }
}
