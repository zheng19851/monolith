package com.runssnail.monolith.dal.executor;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.Transaction;
import org.apache.log4j.Logger;

import com.runssnail.monolith.dal.route.DataSourceRouter;
import com.runssnail.monolith.dal.transaction.SpringManagedTransaction;

/**
 * 
 * @author zhengwei
 */
public abstract class MyBatisBaseExecutor extends BaseExecutor {

    protected final Logger     log    = Logger.getLogger(getClass());

    protected DataSourceRouter router = null;

    protected MyBatisBaseExecutor(Configuration configuration, Transaction transaction, DataSourceRouter router) {
        super(configuration, transaction);
        this.router = router;
    }

    private Connection getConnection(DataSource dataSource, Log statementLog) throws SQLException {
        if (!(transaction instanceof SpringManagedTransaction)) {
            throw new IllegalArgumentException("the Transaction must be SpringManagedTransaction.");
        }
        SpringManagedTransaction monoTransaction = (SpringManagedTransaction) super.transaction;
        monoTransaction.setDataSource(dataSource);

        Connection connection = transaction.getConnection();
        if (statementLog.isDebugEnabled()) {
            return ConnectionLogger.newInstance(connection, statementLog);
        } else {
            return connection;
        }
    }

    public DataSourceRouter getRouter() {
        return router;
    }

    public void setRouter(DataSourceRouter router) {
        this.router = router;
    }

    protected Connection getConnection(MappedStatement ms, Object paramObj, Log statementLog) throws SQLException {
        Connection connection = null;
        DataSource dataSource = router != null ? router.routeDataSource(ms, paramObj) : null;
        if (dataSource != null) {
            connection = getConnection(dataSource, statementLog);
        } else {
            connection = super.getConnection(statementLog);
        }

        return connection;
    }

}
