package com.runssnail.monolith.dal.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import com.runssnail.monolith.dal.route.DataSourceRouter;

/**
 * add {@link DataSourceRouter}, copy from {@link SimpleExecutor}
 * 
 * @author zhengwei
 */
public class MyBatisSimpleExecutor extends MyBatisBaseExecutor {

    public MyBatisSimpleExecutor(Configuration configuration, Transaction transaction, DataSourceRouter router) {
        super(configuration, transaction, router);
    }

    public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null,
                                                                         null);
            stmt = prepareStatement(ms, handler, ms.getStatementLog());
            return handler.update(stmt);
        } finally {
            closeStatement(stmt);
        }
    }

    public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler,
                               BoundSql boundSql) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, rowBounds, resultHandler,
                                                                         boundSql);
            stmt = prepareStatement(ms, handler, ms.getStatementLog());
            return handler.<E> query(stmt, resultHandler);
        } finally {
            closeStatement(stmt);
        }
    }

    public List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException {
        return Collections.emptyList();
    }

    private Statement prepareStatement(MappedStatement ms, StatementHandler handler, Log statementLog)
                                                                                                      throws SQLException {
        Object paramObj = handler.getParameterHandler().getParameterObject();

        Statement stmt;
        Connection connection = super.getConnection(ms, paramObj, statementLog);
        // connection = super.getConnection(statementLog);
        stmt = handler.prepare(connection);
        handler.parameterize(stmt);
        return stmt;
    }

    public DataSourceRouter getRouter() {
        return router;
    }

    public void setRouter(DataSourceRouter router) {
        this.router = router;
    }

}
