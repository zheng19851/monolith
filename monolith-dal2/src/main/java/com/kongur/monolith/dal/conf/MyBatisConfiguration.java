package com.kongur.monolith.dal.conf;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.transaction.Transaction;

import com.kongur.monolith.dal.executor.MyBatisBatchExecutor;
import com.kongur.monolith.dal.executor.MyBatisReuseExecutor;
import com.kongur.monolith.dal.executor.MyBatisSimpleExecutor;
import com.kongur.monolith.dal.route.DataSourceRouter;

/**
 * mybatis≈‰÷√
 * 
 * @author zhengwei
 */
public class MyBatisConfiguration extends Configuration {

    private DataSourceRouter router;

    public MyBatisConfiguration(DataSourceRouter router) {
        this.router = router;
    }

    public Executor newExecutor(Transaction transaction, ExecutorType executorType, boolean autoCommit) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new MyBatisBatchExecutor(this, transaction, router);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new MyBatisReuseExecutor(this, transaction, router);
        } else {
            executor = new MyBatisSimpleExecutor(this, transaction, router);
        }
        if (cacheEnabled) {
            executor = new CachingExecutor(executor, autoCommit);
        }
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }

}
