package com.runssnail.monolith.dal.transaction;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;


/**
 * copy from {@link org.mybatis.spring.transaction.SpringManagedTransactionFactory}
 * 
 * @author zhengwei
 *
 */
public class SpringManagedTransactionFactory implements TransactionFactory {

    /**
     * {@inheritDoc}
     */
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new SpringManagedTransaction(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    public Transaction newTransaction(Connection conn) {
        throw new UnsupportedOperationException("New Spring transactions require a DataSource");
    }

    /**
     * {@inheritDoc}
     */
    public void setProperties(Properties props) {
        // not needed in this version
    }

}
