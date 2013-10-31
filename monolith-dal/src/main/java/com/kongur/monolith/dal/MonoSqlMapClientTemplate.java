package com.kongur.monolith.dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.alibaba.cobar.client.CobarSqlMapClientTemplate;
import com.alibaba.cobar.client.audit.ISqlAuditor;
import com.alibaba.cobar.client.datasources.CobarDataSourceDescriptor;
import com.alibaba.cobar.client.datasources.ICobarDataSourceService;
import com.alibaba.cobar.client.exception.UncategorizedCobarClientException;
import com.alibaba.cobar.client.merger.IMerger;
import com.alibaba.cobar.client.router.ICobarRouter;
import com.alibaba.cobar.client.router.support.IBatisRoutingFact;
import com.alibaba.cobar.client.support.execution.ConcurrentRequest;
import com.alibaba.cobar.client.support.execution.DefaultConcurrentRequestProcessor;
import com.alibaba.cobar.client.support.execution.IConcurrentRequestProcessor;
import com.alibaba.cobar.client.support.utils.CollectionUtils;
import com.alibaba.cobar.client.support.utils.MapUtils;
import com.alibaba.cobar.client.support.utils.Predicate;
import com.alibaba.cobar.client.support.vo.BatchInsertTask;
import com.alibaba.cobar.client.support.vo.CobarMRBase;
import com.alibaba.cobar.client.transaction.MultipleDataSourcesTransactionManager;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.sql.stat.StaticSql;
import com.kongur.monolith.dal.datasources.MonoDataSourceService;
import com.kongur.monolith.dal.router.Router;
import com.kongur.monolith.dal.router.support.RoutingResult;
import com.kongur.monolith.dal.support.domain.Routingable;

/**
 * kongur sqlMapClientTemplate提供访问数据源策略(支持分库分表)
 * 
 * @author zhengwei
 */
@SuppressWarnings("unchecked")
public class MonoSqlMapClientTemplate extends SqlMapClientTemplate implements DisposableBean {

    private transient Logger                     logger                          = LoggerFactory.getLogger(MonoSqlMapClientTemplate.class);

    private List<ExecutorService>                internalExecutorServiceRegistry = new ArrayList<ExecutorService>();
    /**
     * if we want to access multiple database partitions, we need a collection of data source dependencies.<br>
     * {@link ICobarDataSourceService} is a consistent way to get a collection of data source dependencies for @{link
     * CobarSqlMapClientTemplate} and {@link MultipleDataSourcesTransactionManager}.<br>
     * If a router is injected, a dataSourceLocator dependency should be injected too. <br>
     */
    private MonoDataSourceService              kongurDataSourceService;

    /**
     * To enable database partitions access, an {@link ICobarRouter} is a must dependency.<br>
     * if no router is found, the CobarSqlMapClientTemplate will act with behaviors like its parent, the
     * SqlMapClientTemplate.
     */
    private Router<IBatisRoutingFact>            router;

    /**
     * if you want to do SQL auditing, inject an {@link ISqlAuditor} for use.<br>
     * a sibling ExecutorService would be prefered too, which will be used to execute {@link ISqlAuditor}
     * asynchronously.
     */
    private ISqlAuditor                          sqlAuditor;

    /**
     * SQL 审核用的ExecutorService
     */
    private ExecutorService                      sqlAuditorExecutor;

    /**
     * setup ExecutorService for data access requests on each data sources.<br>
     * map key(String) is the identity of DataSource; map value(ExecutorService) is the ExecutorService that will be
     * used to execute query requests on the key's data source.
     */
    private Map<String, ExecutorService>         dataSourceSpecificExecutors     = new HashMap<String, ExecutorService>();

    private IConcurrentRequestProcessor          concurrentRequestProcessor;

    /**
     * timeout threshold to indicate how long the concurrent data access request should time out.<br>
     * time unit in milliseconds.<br>
     */
    private int                                  defaultQueryTimeout             = 100;
    /**
     * indicator to indicate whether to log/profile long-time-running SQL
     */
    private boolean                              profileLongTimeRunningSql       = false;

    private long                                 longTimeRunningSqlIntervalThreshold;

    /**
     * In fact, application can do data-merging in their application code after getting the query result, but they can
     * let {@link CobarSqlMapClientTemplate} do this for them too, as long as they provide a relationship mapping
     * between the sql action and the merging logic provider.
     */
    private Map<String, IMerger<Object, Object>> mergers                         = new HashMap<String, IMerger<Object, Object>>();

    /**
     * 分表成功后，设置到ibatis上下文里的参数名称
     */
    private static final String                  DEFAULT_TABLE_SUFFIX_NAME       = "tableSuffix";

    /**
     * 分表成功后，设置到ibatis上下文里的参数名称, 默认是'tableSuffix'
     */
    private String                               tableSuffixName                 = DEFAULT_TABLE_SUFFIX_NAME;

    /**
     * NOTE: don't use this method for distributed data access.<br>
     * If you are sure that the data access operations will be distributed in a database cluster in the future or even
     * it happens just now, don't use this method, because we can't get enough context information to route these data
     * access operations correctly.
     */
    @Override
    public Object execute(SqlMapClientCallback action) throws DataAccessException {
        return super.execute(action);
    }

    /**
     * NOTE: don't use this method for distributed data access.<br>
     * If you are sure that the data access operations will be distributed in a database cluster in the future or even
     * it happens just now, don't use this method, because we can't get enough context information to route these data
     * access operations correctly.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List executeWithListResult(SqlMapClientCallback action) throws DataAccessException {
        return super.executeWithListResult(action);
    }

    /**
     * NOTE: don't use this method for distributed data access.<br>
     * If you are sure that the data access operations will be distributed in a database cluster in the future or even
     * it happens just now, don't use this method, because we can't get enough context information to route these data
     * access operations correctly.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Map executeWithMapResult(SqlMapClientCallback action) throws DataAccessException {
        return super.executeWithMapResult(action);
    }

    @Override
    public void delete(final String statementName, final Object parameterObject, int requiredRowsAffected)
                                                                                                          throws DataAccessException {
        Integer rowAffected = this.delete(statementName, parameterObject);
        if (rowAffected != requiredRowsAffected) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(statementName, requiredRowsAffected, rowAffected);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int delete(final String statementName, final Object parameterObject) throws DataAccessException {
        auditSqlIfNecessary(statementName, parameterObject);

        long startTimestamp = System.currentTimeMillis();

        try {

            if (parameterObject instanceof Collection) {
                return batchDelete(statementName, (Collection) parameterObject);
            }

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {
                RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
                Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                if (!MapUtils.isEmpty(dsMap)) {

                    // TODO zhengwei modified 2011-12-1
                    SqlMapClientCallback action = new ExecuteSqlMapClientCallbackTemplate(statementName, internalObject) {

                        @Override
                        protected Object doAction(SqlMapExecutor executor, String statement, Object param)
                                                                                                          throws SQLException {
                            return executor.delete(statement, param);
                        }
                    };

                    if (dsMap.size() == 1) {
                        DataSource dataSource = dsMap.get(dsMap.firstKey());
                        Integer delRows = (Integer) executeWith(dataSource, action);
                        return delRows == null ? 0 : delRows.intValue();
                    } else {
                        List<Object> results = executeInConcurrency(action, dsMap);
                        Integer rowAffacted = 0;
                        for (Object item : results) {
                            rowAffacted += (Integer) item;
                        }
                        return rowAffacted;
                    }
                }
            } // end if for partitioning status checking

            if (parameterObject == null) {
                return super.delete(statementName);
            }

            return super.delete(statementName, parameterObject);

        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }
    }

    @Override
    public int delete(String statementName) throws DataAccessException {
        return this.delete(statementName, null);
    }

    /**
     * We support insert in 3 ways here:<br>
     * 
     * <pre>
     *      1- if no partitioning requirement is found:
     *          the insert will be delegated to the default insert behavior of {@link SqlMapClientTemplate};
     *      2- if partitioning support is enabled and 'parameterObject' is NOT a type of collection:
     *          we will search for routing rules against it and execute insertion as per the rule if found, 
     *          if no rule is found, the default data source will be used.
     *      3- if partitioning support is enabled and 'parameterObject' is a type of {@link BatchInsertTask}:
     *           this is a specific solution, mainly aimed for "insert into ..values(), (), ()" style insertion.
     *           In this situation, we will regroup the entities in the original collection into several sub-collections as per routing rules, 
     *           and submit the regrouped sub-collections to their corresponding target data sources.
     *           One thing to NOTE: in this situation, although we return a object as the result of insert, but it doesn't mean any thing to you, 
     *           because, "insert into ..values(), (), ()" style SQL doesn't return you a sensible primary key in this way. 
     *           this, function is optional, although we return a list of sub-insert result, but don't guarantee precise semantics.
     * </pre>
     * 
     * we can't just decide the execution branch on the Collection<?> type of the 'parameterObject', because sometimes,
     * maybe the application does want to do insertion as per the parameterObject of its own.<br>
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object insert(final String statementName, final Object parameterObject) throws DataAccessException {
        auditSqlIfNecessary(statementName, parameterObject);
        long startTimestamp = System.currentTimeMillis();

        try {

            // 批量增加
            if (parameterObject instanceof Collection) {
                return batchInsert(statementName, (Collection) parameterObject);
            }

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {
                /**
                 * sometimes, client will submit batch insert request like "insert into ..values(), (), ()...", it's a
                 * rare situation, but does exist, so we will create new executor on this kind of request processing,
                 * and map each values to their target data source and then reduce to sub-collection, finally, submit
                 * each sub-collection of entities to executor to execute.
                 */
                if (parameterObject != null && parameterObject instanceof BatchInsertTask) {
                    // map collection into mapping of data source and sub collection of entities
                    logger.info("start to prepare batch insert operation with parameter type of:{}.",
                                parameterObject.getClass());

                    return batchInsertAfterReordering(statementName, parameterObject);

                } else {

                    RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName,
                                                                                            parameterObject));
                    SortedMap<String, DataSource> resultDataSources = getDataSources(routingResult.getResourceIdentities());

                    // TODO zhengwei modified 2011-12-1
                    Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                    SqlMapClientCallback action = new ExecuteSqlMapClientCallbackTemplate(statementName, internalObject) {

                        @Override
                        protected Object doAction(SqlMapExecutor executor, String statementName, Object paramObj)
                                                                                                                 throws SQLException {
                            return executor.insert(statementName, paramObj);
                        }
                    };

                    // modified by zhengwei 默认的数据源统一从数据源管理里面取
                    // targetDataSource = getSqlMapClient().getDataSource(); // fall back to default data source.

                    DataSource targetDataSource = null;
                    if (MapUtils.isEmpty(resultDataSources)) {
                        targetDataSource = getKongurDataSourceService().getDefaultDataSource();
                        return executeWith(targetDataSource, action);
                    } else if (resultDataSources.size() == 1) {
                        targetDataSource = resultDataSources.get(resultDataSources.firstKey());
                        return executeWith(targetDataSource, action);
                    }

                    return executeInConcurrency(action, resultDataSources);

                }

            } // end if for partitioning status checking

            // 不分库分表
            // RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
            // Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

            return super.insert(statementName, parameterObject);
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }
    }

    /**
     * 批量插入
     * 
     * @param statementName
     * @param collection
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Object batchInsert(String statementName, Collection collection) {

        // 取第1个数据项作为参数获取数据源
        RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName,
                                                                                collection.iterator().next()));
        SqlMapClientCallback callback = new BatchExecuteSqlMapClientCallbackTemplate(statementName, collection,
                                                                                     routingResult) {

            @Override
            protected void doAction(SqlMapExecutor executor, String statementName, Object paramObj) throws SQLException {
                executor.insert(statementName, paramObj);
            }
        };
        return new Integer(batchAction(statementName, collection, callback, routingResult));
    }

    /**
     * 批量方法
     * 
     * @param statementName
     * @param collection
     * @param callback
     * @param routingResult
     * @return
     */
    @SuppressWarnings("rawtypes")
    private int batchAction(String statementName, Collection collection, SqlMapClientCallback callback,
                            RoutingResult routingResult) {
        int rows = 0;

        SortedMap<String, DataSource> resultDataSources = getDataSources(routingResult.getResourceIdentities());

        // 路由不到数据源的话 ， 就取默认的
        if (resultDataSources == null || resultDataSources.isEmpty()) {
            Integer row = (Integer) executeWith(getKongurDataSourceService().getDefaultDataSource(), callback);
            if (row != null) {
                rows = row.intValue();
            }
        } else {
            for (Map.Entry<String, DataSource> entry : resultDataSources.entrySet()) {
                Integer row = (Integer) executeWith(entry.getValue(), callback);
                if (row != null) {
                    rows += row.intValue();
                }
            }
        }

        return rows;
    }

    /**
     * 批量更新
     * 
     * @param statementName
     * @param collection
     * @return
     */
    @SuppressWarnings("rawtypes")
    private int batchUpdate(String statementName, Collection collection) {

        // 取第1个数据项作为参数获取数据源
        RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName,
                                                                                collection.iterator().next()));
        SqlMapClientCallback callback = new BatchExecuteSqlMapClientCallbackTemplate(statementName, collection,
                                                                                     routingResult) {

            @Override
            protected void doAction(SqlMapExecutor executor, String statementName, Object paramObj) throws SQLException {
                executor.update(statementName, paramObj);
            }
        };
        return new Integer(batchAction(statementName, collection, callback, routingResult));
    }

    /**
     * 批量删除
     * 
     * @param statementName
     * @param collection
     * @return
     */
    @SuppressWarnings("rawtypes")
    private int batchDelete(String statementName, Collection collection) {
        // 取第1个数据项作为参数获取数据源
        RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName,
                                                                                collection.iterator().next()));

        SqlMapClientCallback callback = new BatchExecuteSqlMapClientCallbackTemplate(statementName, collection,
                                                                                     routingResult) {

            @Override
            protected void doAction(SqlMapExecutor executor, String statementName, Object paramObj) throws SQLException {
                executor.delete(statementName, paramObj);
            }
        };
        return new Integer(batchAction(statementName, collection, callback, routingResult));
    }

    /**
     * we reorder the collection of entities in concurrency and commit them in sequence, because we have to conform to
     * the infrastructure of spring's transaction management layer.
     * 
     * @param statementName
     * @param parameterObject
     * @return
     */
    private Object batchInsertAfterReordering(final String statementName, final Object parameterObject) {
        Set<String> keys = new HashSet<String>();
        // keys.add(getDefaultDataSourceName());
        keys.addAll(getKongurDataSourceService().getDataSources().keySet());

        final CobarMRBase mrbase = new CobarMRBase(keys);

        ExecutorService executor = createCustomExecutorService(Runtime.getRuntime().availableProcessors(),
                                                               "batchInsertAfterReordering");
        try {
            final StringBuffer exceptionStaktrace = new StringBuffer();

            Collection<?> paramCollection = ((BatchInsertTask) parameterObject).getEntities();

            final CountDownLatch latch = new CountDownLatch(paramCollection.size());

            Iterator<?> iter = paramCollection.iterator();
            while (iter.hasNext()) {
                final Object entity = iter.next();
                Runnable task = new Runnable() {

                    public void run() {
                        try {

                            RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName,
                                                                                                    entity));
                            SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                            // TODO zhengwei modified 2011-12-1
                            Object internalObject = convert2InternalParamObject(entity, routingResult);

                            if (MapUtils.isEmpty(dsMap)) {
                                logger.info("can't find routing rule for {} with parameter {}, so use default data source for it.",
                                            statementName, internalObject);
                                // mrbase.emit(getDefaultDataSourceName(), internalObject);
                                mrbase.emit(getKongurDataSourceService().getDefaultDataSourceDescriptor().getIdentity(),
                                            internalObject);
                            } else {
                                if (dsMap.size() > 1) {
                                    throw new IllegalArgumentException(
                                                                       "unexpected routing result, found more than 1 target data source for current entity:"
                                                                               + internalObject);
                                }
                                mrbase.emit(dsMap.firstKey(), internalObject);
                            }
                        } catch (Throwable t) {
                            exceptionStaktrace.append(ExceptionUtils.getFullStackTrace(t));
                        } finally {
                            latch.countDown();
                        }
                    }
                };
                executor.execute(task);
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new ConcurrencyFailureException(
                                                      "unexpected interruption when re-arranging parameter collection into sub-collections ",
                                                      e);
            }

            if (exceptionStaktrace.length() > 0) {
                throw new ConcurrencyFailureException(
                                                      "unpected exception when re-arranging parameter collection, check previous log for details.\n"
                                                              + exceptionStaktrace);
            }
        } finally {
            executor.shutdown();
        }

        List<ConcurrentRequest> requests = new ArrayList<ConcurrentRequest>();
        for (Map.Entry<String, List<Object>> entity : mrbase.getResources().entrySet()) {
            final List<Object> paramList = entity.getValue();
            if (CollectionUtils.isEmpty(paramList)) {
                continue;
            }

            String identity = entity.getKey();

            final DataSource dataSourceToUse = getKongurDataSourceService().getDataSource(identity);

            final SqlMapClientCallback callback = new SqlMapClientCallback() {

                public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                    return executor.insert(statementName, paramList);
                }
            };

            ConcurrentRequest request = new ConcurrentRequest();
            request.setDataSource(dataSourceToUse);
            request.setAction(callback);
            request.setExecutor(getDataSourceSpecificExecutors().get(identity));
            requests.add(request);
        }
        return getConcurrentRequestProcessor().process(requests);
    }

    @Override
    public Object insert(String statementName) throws DataAccessException {
        return this.insert(statementName, null);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List queryForList(String statementName, int skipResults, int maxResults) throws DataAccessException {
        return this.queryForList(statementName, null, skipResults, maxResults);
    }

    @SuppressWarnings("rawtypes")
    protected List queryForList(final String statementName, final Object parameterObject, final Integer skipResults,
                                final Integer maxResults) {
        auditSqlIfNecessary(statementName, parameterObject);

        long startTimestamp = System.currentTimeMillis();

        try {

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {

                RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
                Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                if (!MapUtils.isEmpty(dsMap)) {

                    // TODO zhengwei modified 2011-12-1
                    SqlMapClientCallback callback = new InternalSqlMapClientCallback1(statementName, internalObject,
                                                                                      skipResults, maxResults);

                    if (dsMap.size() == 1) {
                        return (List) executeWith(dsMap.get(dsMap.firstKey()), callback);
                    } else {
                        List<Object> originalResultList = executeInConcurrency(callback, dsMap);
                        if (MapUtils.isNotEmpty(getMergers()) && getMergers().containsKey(statementName)) {
                            IMerger<Object, Object> merger = getMergers().get(statementName);
                            if (merger != null) {
                                return (List) merger.merge(originalResultList);
                            }
                        }

                        List<Object> resultList = new ArrayList<Object>();
                        for (Object item : originalResultList) {
                            resultList.addAll((List) item);
                        }
                        return resultList;
                    }

                }
            } // end if for partitioning status checking

            // 不分库分表
            if (skipResults == null || maxResults == null) {
                return super.queryForList(statementName, parameterObject);
            } else {
                return super.queryForList(statementName, parameterObject, skipResults, maxResults);
            }
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List queryForList(final String statementName, final Object parameterObject, final int skipResults,
                             final int maxResults) throws DataAccessException {
        return this.queryForList(statementName, parameterObject, Integer.valueOf(skipResults),
                                 Integer.valueOf(maxResults));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List queryForList(final String statementName, final Object parameterObject) throws DataAccessException {
        return this.queryForList(statementName, parameterObject, null, null);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List queryForList(String statementName) throws DataAccessException {
        return this.queryForList(statementName, null);
    }

    @Override
    public Map<Object, Object> queryForMap(final String statementName, final Object parameterObject,
                                           final String keyProperty, final String valueProperty)
                                                                                                throws DataAccessException {
        auditSqlIfNecessary(statementName, parameterObject);
        long startTimestamp = System.currentTimeMillis();
        try {

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {

                RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
                final Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                if (!MapUtils.isEmpty(dsMap)) {
                    SqlMapClientCallback callback = null;
                    if (valueProperty != null) {
                        callback = new SqlMapClientCallback() {

                            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                                return executor.queryForMap(statementName, internalObject, keyProperty, valueProperty);
                            }
                        };
                    } else {
                        callback = new SqlMapClientCallback() {

                            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                                return executor.queryForMap(statementName, internalObject, keyProperty);
                            }
                        };
                    }

                    if (dsMap.size() == 1) {
                        return (Map<Object, Object>) executeWith(dsMap.get(dsMap.firstKey()), callback);
                    } else {
                        List<Object> originalResults = executeInConcurrency(callback, dsMap);
                        Map<Object, Object> resultMap = new HashMap<Object, Object>();
                        for (Object item : originalResults) {
                            resultMap.putAll((Map<?, ?>) item);
                        }
                        return resultMap;
                    }

                }
            } // end if for partitioning status checking

            // 不分库分表
            if (valueProperty == null) {
                return super.queryForMap(statementName, parameterObject, keyProperty);
            } else {
                return super.queryForMap(statementName, parameterObject, keyProperty, valueProperty);
            }
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }

    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map queryForMap(final String statementName, final Object parameterObject, final String keyProperty)
                                                                                                              throws DataAccessException {
        return this.queryForMap(statementName, parameterObject, keyProperty, null);
    }

    @Override
    public Object queryForObject(final String statementName, final Object parameterObject, final Object resultObject)
                                                                                                                     throws DataAccessException {
        auditSqlIfNecessary(statementName, parameterObject);
        long startTimestamp = System.currentTimeMillis();

        try {

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {

                // 根据SQL ID和参数对请求进行路由
                RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
                Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                if (!MapUtils.isEmpty(dsMap)) {

                    // TODO zhengwei modified 2011-12-1
                    SqlMapClientCallback callback = new InternalSqlMapClientCallback0(statementName, internalObject,
                                                                                      resultObject);

                    if (dsMap.size() == 1) {
                        return executeWith(dsMap.get(dsMap.firstKey()), callback);
                    }

                    List<Object> resultList = executeInConcurrency(callback, dsMap);
                    Collection<Object> filteredResultList = CollectionUtils.select(resultList, new Predicate() {

                        public boolean evaluate(Object item) {
                            return item != null;
                        }
                    });
                    if (filteredResultList.size() > 1) {
                        throw new IncorrectResultSizeDataAccessException(1);
                    }
                    if (CollectionUtils.isEmpty(filteredResultList)) {
                        return null;
                    }
                    return filteredResultList.iterator().next();
                }
            } // end if for partitioning status checking

            // 不分库分表
            if (resultObject == null) {
                return super.queryForObject(statementName, parameterObject);
            } else {
                return super.queryForObject(statementName, parameterObject, resultObject);
            }

        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }
    }

    /**
     * 如果路由结果的表后缀非空，就将参数对象转成内部对象(除基本数据类型和MAP)
     * 
     * @param parameterObject
     * @param routingResult
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected Object convert2InternalParamObject(Object parameterObject, RoutingResult routingResult) {

        if (parameterObject == null) {
            return null;
        }

        String tableSuffix = routingResult.getTableSuffix();
        Object internalObject = parameterObject;

        if (StringUtils.isNotBlank(tableSuffix)) {
            if (logger.isDebugEnabled()) {
                logger.debug("resolve table suffix=" + tableSuffix);
            }

            if (internalObject instanceof Routingable) {
                // try {
                // MethodUtils.invokeExactMethod(internalObject, "setTableSuffix", tableSuffix);
                // } catch (Exception e) {
                // logger.error("invoke setTableName method error, parameterObject=" + internalObject, e);
                // }
                Routingable routingable = (Routingable) internalObject;
                routingable.setTableSuffix(tableSuffix);
            } else if (isNeedConvert2Map(internalObject)) {
                Map internalParams = null;
                try {
                    internalParams = convert2Map(internalObject);
                } catch (Exception e) {
                    logger.error("convert2Map error, parameterObject=" + internalObject, e);
                }

                if (internalParams != null) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("internalParams=" + internalParams);
                    }

                    internalParams.put(tableSuffixName, tableSuffix);
                    internalObject = internalParams;
                }
            }
        }

        return internalObject;
    }

    protected boolean isNeedConvert2Map(Object parameterObject) {
        boolean isLiteralType = true;

        if (parameterObject instanceof Long) {
            isLiteralType = false;
        } else if (parameterObject instanceof String) {
            isLiteralType = false;
        } else if (parameterObject instanceof Double) {
            isLiteralType = false;
        } else if (parameterObject instanceof Float) {
            isLiteralType = false;
        } else if (parameterObject instanceof Integer) {
            isLiteralType = false;
        } else if (parameterObject instanceof Short) {
            isLiteralType = false;
        } else if (parameterObject instanceof Character) {
            isLiteralType = false;
        } else if (parameterObject instanceof Byte) {
            isLiteralType = false;
        }
        return isLiteralType;
    }

    @SuppressWarnings("rawtypes")
    protected Map convert2Map(Object parameterObject) throws Exception {
        if (parameterObject instanceof Map) {
            return (Map) parameterObject;
        } else {
            return org.apache.commons.beanutils.BeanUtils.describe(parameterObject);
        }

    }

    @Override
    public Object queryForObject(final String statementName, final Object parameterObject) throws DataAccessException {
        return this.queryForObject(statementName, parameterObject, null);
    }

    @Override
    public Object queryForObject(String statementName) throws DataAccessException {
        return this.queryForObject(statementName, null);
    }

    @Override
    public void queryWithRowHandler(final String statementName, final Object parameterObject,
                                    final RowHandler rowHandler) throws DataAccessException {
        auditSqlIfNecessary(statementName, parameterObject);

        long startTimestamp = System.currentTimeMillis();

        try {

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {

                RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
                final Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                if (!MapUtils.isEmpty(dsMap)) {
                    SqlMapClientCallback callback = null;
                    if (internalObject == null) {
                        callback = new SqlMapClientCallback() {

                            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                                executor.queryWithRowHandler(statementName, rowHandler);
                                return null;
                            }
                        };
                    } else {
                        callback = new SqlMapClientCallback() {

                            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                                executor.queryWithRowHandler(statementName, internalObject, rowHandler);
                                return null;
                            }
                        };
                    }

                    if (dsMap.size() == 1) {
                        executeWith(dsMap.get(dsMap.firstKey()), callback);
                        return;
                    }

                    executeInConcurrency(callback, dsMap);
                    return;
                }
            } // end if for partitioning status checking

            // 不分库分表
            if (parameterObject == null) {
                super.queryWithRowHandler(statementName, rowHandler);
            } else {
                super.queryWithRowHandler(statementName, parameterObject, rowHandler);
            }

        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }
    }

    @Override
    public void queryWithRowHandler(String statementName, RowHandler rowHandler) throws DataAccessException {
        this.queryWithRowHandler(statementName, null, rowHandler);
    }

    @Override
    public void update(String statementName, Object parameterObject, int requiredRowsAffected)
                                                                                              throws DataAccessException {
        int rowAffected = this.update(statementName, parameterObject);
        if (rowAffected != requiredRowsAffected) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(statementName, requiredRowsAffected, rowAffected);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int update(final String statementName, final Object parameterObject) throws DataAccessException {
        auditSqlIfNecessary(statementName, parameterObject);

        long startTimestamp = System.currentTimeMillis();

        try {

            if (parameterObject instanceof Collection) {
                return batchUpdate(statementName, (Collection) parameterObject);
            }

            // 分库分表
            if (isPartitioningBehaviorEnabled()) {

                RoutingResult routingResult = getRouter().doRoute(new IBatisRoutingFact(statementName, parameterObject));
                Object internalObject = convert2InternalParamObject(parameterObject, routingResult);

                SortedMap<String, DataSource> dsMap = getDataSources(routingResult.getResourceIdentities());

                if (!MapUtils.isEmpty(dsMap)) {

                    // TODO zhengwei modified 2011-12-1
                    SqlMapClientCallback action = new ExecuteSqlMapClientCallbackTemplate(statementName, internalObject) {

                        @Override
                        protected Object doAction(SqlMapExecutor executor, String statement, Object param)
                                                                                                          throws SQLException {
                            return executor.update(statement, param);
                        }
                    };

                    if (dsMap.size() == 1) {
                        Integer rows = (Integer) executeWith(dsMap.get(dsMap.firstKey()), action);
                        return rows == null ? 0 : rows.intValue();
                    }

                    List<Object> results = executeInConcurrency(action, dsMap);
                    Integer rowAffacted = 0;

                    for (Object item : results) {
                        rowAffacted += (Integer) item;
                    }
                    return rowAffacted;
                }
            } // end if for partitioning status checking

            return super.update(statementName, parameterObject);

        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn("SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                                new Object[] { statementName, parameterObject, interval });
                }
            }
        }
    }

    @Override
    public int update(String statementName) throws DataAccessException {
        return this.update(statementName, null);
    }

    protected SortedMap<String, DataSource> getDataSources(List<String> resourceIdentities) {

        return getKongurDataSourceService().getDataSourses(resourceIdentities, true);
    }

    protected String getSqlByStatementName(String statementName, Object parameterObject) {
        SqlMapClientImpl sqlMapClientImpl = (SqlMapClientImpl) getSqlMapClient();
        Sql sql = sqlMapClientImpl.getMappedStatement(statementName).getSql();
        if (sql instanceof StaticSql) {
            return sql.getSql(null, parameterObject);
        } else {
            logger.info("dynamic sql can only return sql id.");
            return statementName;
        }
    }

    protected Object executeWith(DataSource dataSource, SqlMapClientCallback action) {
        SqlMapSession session = getSqlMapClient().openSession();

        try {
            Connection springCon = null;
            boolean transactionAware = (dataSource instanceof TransactionAwareDataSourceProxy);

            // Obtain JDBC Connection to operate on...
            try {
                springCon = (transactionAware ? dataSource.getConnection() : DataSourceUtils.doGetConnection(dataSource));
                session.setUserConnection(springCon);
            } catch (SQLException ex) {
                throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
            }

            try {
                return action.doInSqlMapClient(session);
            } catch (SQLException ex) {
                throw new SQLErrorCodeSQLExceptionTranslator().translate("SqlMapClient operation", null, ex);
            } catch (Throwable t) {
                throw new UncategorizedCobarClientException("unknown excepton when performing data access operation.",
                                                            t);
            } finally {
                try {
                    if (springCon != null) {
                        if (transactionAware) {
                            springCon.close();
                        } else {
                            DataSourceUtils.doReleaseConnection(springCon, dataSource);
                        }
                    }
                } catch (Throwable ex) {
                    logger.debug("Could not close JDBC Connection", ex);
                }
            }
            // Processing finished - potentially session still to be closed.
        } finally {
            session.close();
        }
    }

    public List<Object> executeInConcurrency(SqlMapClientCallback action, SortedMap<String, DataSource> dsMap) {
        List<ConcurrentRequest> requests = new ArrayList<ConcurrentRequest>();

        for (Map.Entry<String, DataSource> entry : dsMap.entrySet()) {
            ConcurrentRequest request = new ConcurrentRequest();
            request.setAction(action);
            request.setDataSource(entry.getValue());
            request.setExecutor(getDataSourceSpecificExecutors().get(entry.getKey()));
            requests.add(request);
        }

        List<Object> results = getConcurrentRequestProcessor().process(requests);
        return results;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (isProfileLongTimeRunningSql()) {
            if (longTimeRunningSqlIntervalThreshold <= 0) {
                throw new IllegalArgumentException(
                                                   "'longTimeRunningSqlIntervalThreshold' should have a positive value if 'profileLongTimeRunningSql' is set to true");
            }
        }
        setupDefaultExecutorServicesIfNecessary();
        setUpDefaultSqlAuditorExecutorIfNecessary();
        if (getConcurrentRequestProcessor() == null) {
            setConcurrentRequestProcessor(new DefaultConcurrentRequestProcessor(getSqlMapClient()));
        }

        // 设置默认数据源，如果这里不设置的话，程序就就会取设置在SqlMapClient里的DataSource
        // CobarDataSourceDescriptor defaultDataSourceDesc =
        // getKongurDataSourceService().getDefaultDataSourceDescriptor();
        // if (defaultDataSourceDesc != null) {
        // setDefaultDataSourceName(defaultDataSourceDesc.getIdentity());
        // }
    }

    public void destroy() throws Exception {
        if (CollectionUtils.isNotEmpty(internalExecutorServiceRegistry)) {
            logger.info("shutdown executors of CobarSqlMapClientTemplate...");
            for (ExecutorService executor : internalExecutorServiceRegistry) {
                if (executor != null) {
                    try {
                        executor.shutdown();
                        executor.awaitTermination(5, TimeUnit.MINUTES);
                        executor = null;
                    } catch (InterruptedException e) {
                        logger.warn("interrupted when shuting down the query executor:\n{}", e);
                    }
                }
            }
            getDataSourceSpecificExecutors().clear();
            logger.info("all of the executor services in CobarSqlMapClientTemplate are disposed.");
        }
    }

    /**
     * if a SqlAuditor is injected and a sqlAuditorExecutor is NOT provided together, we need to setup a
     * sqlAuditorExecutor so that the SQL auditing actions can be performed asynchronously. <br>
     * otherwise, the data access process may be blocked by auditing SQL.<br>
     * Although an external ExecutorService can be injected for use, normally, it's not so necessary.<br>
     * Most of the time, you should inject an proper {@link ISqlAuditor} which will do SQL auditing in a asynchronous
     * way.<br>
     */
    private void setUpDefaultSqlAuditorExecutorIfNecessary() {
        if (sqlAuditor != null && sqlAuditorExecutor == null) {
            sqlAuditorExecutor = createCustomExecutorService(1, "setUpDefaultSqlAuditorExecutorIfNecessary");
            // 1. register executor for disposing later explicitly
            internalExecutorServiceRegistry.add(sqlAuditorExecutor);
            // 2. dispose executor implicitly
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    if (sqlAuditorExecutor == null) {
                        return;
                    }
                    try {
                        sqlAuditorExecutor.shutdown();
                        sqlAuditorExecutor.awaitTermination(5, TimeUnit.MINUTES);
                    } catch (InterruptedException e) {
                        logger.warn("interrupted when shuting down the query executor:\n{}", e);
                    }
                }
            });
        }
    }

    /**
     * If more than one data sources are involved in a data access request, we need a collection of executors to execute
     * the request on these data sources in parallel.<br>
     * But in case the users forget to inject a collection of executors for this purpose, we need to setup a default
     * one.<br>
     */
    private void setupDefaultExecutorServicesIfNecessary() {

        if (isPartitioningBehaviorEnabled()) {

            if (MapUtils.isEmpty(getDataSourceSpecificExecutors())) {

                Set<CobarDataSourceDescriptor> dataSourceDescriptors = getKongurDataSourceService().getDataSourceDescriptors();
                for (CobarDataSourceDescriptor descriptor : dataSourceDescriptors) {
                    ExecutorService executor = createExecutorForSpecificDataSource(descriptor);
                    getDataSourceSpecificExecutors().put(descriptor.getIdentity(), executor);
                }
            }

            // 如果存在当前默认的数据源就不需要重复加入了
            // if (!getKongurDataSourceService().contains(getDefaultDataSourceName())) {
            // addDefaultSingleThreadExecutorIfNecessary();
            // }

        }
    }

    private ExecutorService createExecutorForSpecificDataSource(CobarDataSourceDescriptor descriptor) {
        final String identity = descriptor.getIdentity();
        final ExecutorService executor = createCustomExecutorService(descriptor.getPoolSize(),
                                                                     "createExecutorForSpecificDataSource-" + identity
                                                                             + " data source");
        // 1. register executor for disposing explicitly
        internalExecutorServiceRegistry.add(executor);
        // 2. dispose executor implicitly
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if (executor == null) {
                    return;
                }

                try {
                    executor.shutdown();
                    executor.awaitTermination(5, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    logger.warn("interrupted when shuting down the query executor:\n{}", e);
                }
            }
        });
        return executor;
    }

    // private void addDefaultSingleThreadExecutorIfNecessary() {
    // String identity = getDefaultDataSourceName();
    // CobarDataSourceDescriptor descriptor = new CobarDataSourceDescriptor();
    // descriptor.setIdentity(identity);
    // descriptor.setPoolSize(Runtime.getRuntime().availableProcessors() * 5);
    // getDataSourceSpecificExecutors().put(identity, createExecutorForSpecificDataSource(descriptor));
    // }

    protected void auditSqlIfNecessary(final String statementName, final Object parameterObject) {
        if (getSqlAuditor() != null) {
            getSqlAuditorExecutor().execute(new Runnable() {

                public void run() {
                    getSqlAuditor().audit(statementName, getSqlByStatementName(statementName, parameterObject),
                                          parameterObject);
                }
            });
        }
    }

    /**
     * if a router and a data source locator is provided, it means data access on different databases is enabled.<br>
     */
    protected boolean isPartitioningBehaviorEnabled() {
        return ((router != null) && (getKongurDataSourceService() != null));
    }

    /**
     * 是否打开路由功能
     * 
     * @return
     */
    public boolean isRouterEnabled() {
        return router != null;
    }

    public void setSqlAuditor(ISqlAuditor sqlAuditor) {
        this.sqlAuditor = sqlAuditor;
    }

    public ISqlAuditor getSqlAuditor() {
        return sqlAuditor;
    }

    public void setSqlAuditorExecutor(ExecutorService sqlAuditorExecutor) {
        this.sqlAuditorExecutor = sqlAuditorExecutor;
    }

    public ExecutorService getSqlAuditorExecutor() {
        return sqlAuditorExecutor;
    }

    public void setDataSourceSpecificExecutors(Map<String, ExecutorService> dataSourceSpecificExecutors) {
        if (MapUtils.isEmpty(dataSourceSpecificExecutors)) {
            return;
        }
        this.dataSourceSpecificExecutors = dataSourceSpecificExecutors;
    }

    public Map<String, ExecutorService> getDataSourceSpecificExecutors() {
        return dataSourceSpecificExecutors;
    }

    public void setDefaultQueryTimeout(int defaultQueryTimeout) {
        this.defaultQueryTimeout = defaultQueryTimeout;
    }

    public int getDefaultQueryTimeout() {
        return defaultQueryTimeout;
    }

    public MonoDataSourceService getKongurDataSourceService() {
        return kongurDataSourceService;
    }

    public void setKongurDataSourceService(MonoDataSourceService kongurDataSourceService) {
        this.kongurDataSourceService = kongurDataSourceService;
    }

    public void setProfileLongTimeRunningSql(boolean profileLongTimeRunningSql) {
        this.profileLongTimeRunningSql = profileLongTimeRunningSql;
    }

    public boolean isProfileLongTimeRunningSql() {
        return profileLongTimeRunningSql;
    }

    public void setLongTimeRunningSqlIntervalThreshold(long longTimeRunningSqlIntervalThreshold) {
        this.longTimeRunningSqlIntervalThreshold = longTimeRunningSqlIntervalThreshold;
    }

    public long getLongTimeRunningSqlIntervalThreshold() {
        return longTimeRunningSqlIntervalThreshold;
    }

    public void setRouter(Router<IBatisRoutingFact> router) {
        this.router = router;
    }

    public Router<IBatisRoutingFact> getRouter() {
        return router;
    }

    public void setConcurrentRequestProcessor(IConcurrentRequestProcessor concurrentRequestProcessor) {
        this.concurrentRequestProcessor = concurrentRequestProcessor;
    }

    public IConcurrentRequestProcessor getConcurrentRequestProcessor() {
        return concurrentRequestProcessor;
    }

    public void setMergers(Map<String, IMerger<Object, Object>> mergers) {
        this.mergers = mergers;
    }

    public Map<String, IMerger<Object, Object>> getMergers() {
        return mergers;
    }

    private ExecutorService createCustomExecutorService(int poolSize, final String method) {
        int coreSize = Runtime.getRuntime().availableProcessors();
        if (poolSize < coreSize) {
            coreSize = poolSize;
        }
        ThreadFactory tf = new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "thread created at CobarSqlMapClientTemplate method [" + method + "]");
                t.setDaemon(true);
                return t;
            }
        };
        BlockingQueue<Runnable> queueToUse = new LinkedBlockingQueue<Runnable>();
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, poolSize, 60, TimeUnit.SECONDS,
                                                                   queueToUse, tf,
                                                                   new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }

    /**
     * 增加、删除、修改用的模板
     * 
     * @author zhengwei
     */
    abstract class ExecuteSqlMapClientCallbackTemplate implements SqlMapClientCallback {

        String statementName;
        Object parameterObject;

        ExecuteSqlMapClientCallbackTemplate(String statementName, Object parameterObject) {
            this.statementName = statementName;
            this.parameterObject = parameterObject;
        }

        public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
            return doAction(executor, statementName, parameterObject);
        }

        protected abstract Object doAction(SqlMapExecutor executor, String statement, Object param) throws SQLException;

    }

    /**
     * @author zhengwei
     */
    class InternalSqlMapClientCallback1 extends InternalSqlMapClientCallback0 implements SqlMapClientCallback {

        Integer skipResults;
        Integer maxResults;

        InternalSqlMapClientCallback1(String statementName, Object parameterObject, Integer skipResults,
                                      Integer maxResults) {
            super(statementName, parameterObject);
            this.skipResults = skipResults;
            this.maxResults = maxResults;
        }

        public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
            if (skipResults != null && maxResults != null) {
                return executor.queryForList(statementName, parameterObject, skipResults, maxResults);
            } else {
                return executor.queryForList(statementName, parameterObject);
            }
        }

    }

    /**
     * @author zhengwei
     */
    class InternalSqlMapClientCallback0 implements SqlMapClientCallback {

        String statementName;

        Object parameterObject;

        Object returnObject;

        InternalSqlMapClientCallback0(String statementName, Object parameterObject) {
            this.statementName = statementName;
            this.parameterObject = parameterObject;
        }

        InternalSqlMapClientCallback0(String statementName, Object parameterObject, Object returnObject) {
            this.statementName = statementName;
            this.parameterObject = parameterObject;
            this.returnObject = returnObject;
        }

        public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {

            if (returnObject != null) {
                return executor.queryForObject(statementName, parameterObject, returnObject);
            } else {
                return executor.queryForObject(statementName, parameterObject);
            }
        }
    }

    /**
     * 批量操作用的callback 模板
     * 
     * @author zhengwei
     */
    @SuppressWarnings("rawtypes")
    abstract class BatchExecuteSqlMapClientCallbackTemplate implements SqlMapClientCallback {

        String        statementName;
        Collection    collection;
        RoutingResult routingResult;

        BatchExecuteSqlMapClientCallbackTemplate(String statementName, Collection collection,
                                                 RoutingResult routingResult) {
            this.statementName = statementName;
            this.collection = collection;
            this.routingResult = routingResult;
        }

        public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
            executor.startBatch();
            for (Object param : collection) {
                doAction(executor, statementName, convert2InternalParamObject(param, routingResult));
            }
            return executor.executeBatch();

        }

        protected abstract void doAction(SqlMapExecutor executor, String statementName, Object paramObj)
                                                                                                        throws SQLException;
    }

    public String getTableSuffixName() {
        return tableSuffixName;
    }

    public void setTableSuffixName(String tableSuffixName) {
        this.tableSuffixName = tableSuffixName;
    }

}
