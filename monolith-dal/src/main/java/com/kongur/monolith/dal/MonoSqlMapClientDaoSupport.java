package com.kongur.monolith.dal;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.alibaba.cobar.client.CobarSqlMapClientDaoSupport;

/**
 * @author zhengwei
 */
public class MonoSqlMapClientDaoSupport extends CobarSqlMapClientDaoSupport {

    protected boolean isPartitionBehaviorEnabled() {
        if (getSqlMapClientTemplate() instanceof MonoSqlMapClientTemplate) {
            return ((MonoSqlMapClientTemplate) getSqlMapClientTemplate()).isPartitioningBehaviorEnabled();
        }
        return false;
    }

    public int batchInsert(final String statementName, final Collection<?> entities) throws DataAccessException {

        int counter = 0;
        DataAccessException lastEx = null;
        for (Object parameterObject : entities) {
            try {
                getSqlMapClientTemplate().insert(statementName, parameterObject);
                counter++;
            } catch (DataAccessException e) {
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw lastEx;
        }
        return counter;

    }

    public int batchDelete(final String statementName, final Collection<?> entities) throws DataAccessException {

        int counter = 0;
        DataAccessException lastEx = null;
        for (Object entity : entities) {
            try {
                counter += getSqlMapClientTemplate().delete(statementName, entity);
            } catch (DataAccessException e) {
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw lastEx;
        }
        return counter;

    }

    public int batchUpdate(final String statementName, final Collection<?> entities) throws DataAccessException {

        int counter = 0;
        DataAccessException lastEx = null;
        for (Object parameterObject : entities) {
            try {
                counter += getSqlMapClientTemplate().update(statementName, parameterObject);
            } catch (DataAccessException e) {
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw lastEx;
        }
        return counter;

    }
}
