package com.kongur.monolith.dal.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import com.kongur.monolith.dal.route.DataSourceRouter;

/**
 * copy from {@link ReuseExecutor}
 * 
 * @author zhengwei
 */
public class MyBatisReuseExecutor extends MyBatisBaseExecutor {

    private final Map<String, Statement> statementMap = new HashMap<String, Statement>();

    public MyBatisReuseExecutor(Configuration configuration, Transaction transaction, DataSourceRouter router) {
        super(configuration, transaction, router);
    }

    public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
        Statement stmt = prepareStatement(ms, handler, ms.getStatementLog());
        return handler.update(stmt);
    }

    public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler,
                               BoundSql boundSql) throws SQLException {
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, rowBounds, resultHandler,
                                                                     boundSql);
        Statement stmt = prepareStatement(ms, handler, ms.getStatementLog());
        return handler.<E> query(stmt, resultHandler);
    }

    public List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException {
        for (Statement stmt : statementMap.values()) {
            closeStatement(stmt);
        }
        statementMap.clear();
        return Collections.emptyList();
    }

    private Statement prepareStatement(MappedStatement ms, StatementHandler handler, Log statementLog)
                                                                                                      throws SQLException {
        Statement stmt;
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();
        if (hasStatementFor(sql)) {
            stmt = getStatement(sql);
        } else {
            // Connection connection = getConnection(statementLog);

            Object paramObj = handler.getParameterHandler().getParameterObject();
            Connection connection = super.getConnection(ms, paramObj, statementLog);

            // connection = super.getConnection(statementLog);
            stmt = handler.prepare(connection);
            putStatement(sql, stmt);
        }
        handler.parameterize(stmt);
        return stmt;
    }

    private boolean hasStatementFor(String sql) {
        try {
            return statementMap.keySet().contains(sql) && !statementMap.get(sql).getConnection().isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private Statement getStatement(String s) {
        return statementMap.get(s);
    }

    private void putStatement(String sql, Statement stmt) {
        statementMap.put(sql, stmt);
    }

}
