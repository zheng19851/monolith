package com.runssnail.monolith.dal.transaction;

import static org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection;
import static org.springframework.util.Assert.notNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * copy from {@link org.mybatis.spring.transaction.SpringManagedTransaction}, 因为{@link org.mybatis.spring.transaction.SpringManagedTransaction}不能替换dataSource，所以重新建立这个类
 * 
 * @author zhengwei
 */
public class SpringManagedTransaction implements Transaction {

    private static final Log logger = LogFactory.getLog(SpringManagedTransaction.class);

    private DataSource       dataSource;

    private Connection       connection;

    private boolean          isConnectionTransactional;

    private boolean          autoCommit;

    public SpringManagedTransaction(DataSource dataSource) {
        notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() throws SQLException {
        if (this.connection == null) {
            openConnection();
        }
        return this.connection;
    }

    /**
     * Gets a connection from Spring transaction manager and discovers if this {@code Transaction} should manage
     * connection or let it to Spring.
     * <p>
     * It also reads autocommit setting because when using Spring Transaction MyBatis thinks that autocommit is always
     * false and will always call commit/rollback so we need to no-op that calls.
     */
    private void openConnection() throws SQLException {
        this.connection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.connection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

        if (logger.isDebugEnabled()) {
            logger.debug("JDBC Connection [" + this.connection + "] will"
                         + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void commit() throws SQLException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            if (logger.isDebugEnabled()) {
                logger.debug("Committing JDBC Connection [" + this.connection + "]");
            }
            this.connection.commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void rollback() throws SQLException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            if (logger.isDebugEnabled()) {
                logger.debug("Rolling back JDBC Connection [" + this.connection + "]");
            }
            this.connection.rollback();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws SQLException {
        releaseConnection(this.connection, this.dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isConnectionTransactional() {
        return isConnectionTransactional;
    }

    public void setConnectionTransactional(boolean isConnectionTransactional) {
        this.isConnectionTransactional = isConnectionTransactional;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
