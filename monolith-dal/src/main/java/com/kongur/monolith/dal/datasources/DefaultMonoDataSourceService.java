package com.kongur.monolith.dal.datasources;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.springframework.util.Assert;

import com.alibaba.cobar.client.datasources.CobarDataSourceDescriptor;
import com.alibaba.cobar.client.datasources.DefaultCobarDataSourceService;
import com.alibaba.cobar.client.datasources.ha.NonHADataSourceCreator;
import com.alibaba.cobar.client.support.utils.CollectionUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 默认的数据源管理实现
 * 
 * @author zhengwei
 */
public class DefaultMonoDataSourceService extends DefaultCobarDataSourceService implements MonoDataSourceService {

    /**
     * add by zhengwei 默认的数据源
     */
    private DataSource                defaultDataSource;

    /**
     * add by zhengwei 默认的数据源配置
     */
    private CobarDataSourceDescriptor defaultDataSourceDescriptor;

    private SqlMapClient              sqlMapClient;

    /**
     * 默认的数据源ID
     */
    private String                    defaultDataSourceName = "default";

    public void afterPropertiesSet() throws Exception {

        if (getHaDataSourceCreator() == null) {
            setHaDataSourceCreator(new NonHADataSourceCreator());
        }
        if (CollectionUtils.isEmpty(getDataSourceDescriptors())) {
            return;
        }

        for (CobarDataSourceDescriptor descriptor : getDataSourceDescriptors()) {

            MonoDataSourceDescriptor kongurDescriptor = (MonoDataSourceDescriptor) descriptor;

            Validate.notEmpty(descriptor.getIdentity());
            Validate.notNull(descriptor.getTargetDataSource());

            DataSource dataSourceToUse = descriptor.getTargetDataSource();

            if (descriptor.getStandbyDataSource() != null) {
                dataSourceToUse = getHaDataSourceCreator().createHADataSource(descriptor);
            }

            // DataSource proxyDataSource = new LazyConnectionDataSourceProxy(dataSourceToUse);
            // getDataSources().put(descriptor.getIdentity(), proxyDataSource);

            getDataSources().put(descriptor.getIdentity(), dataSourceToUse);
            if (kongurDescriptor.isDefaultDataSource()) {
                defaultDataSource = dataSourceToUse;
                defaultDataSourceDescriptor = descriptor;
            }
        }

        // 将sqlmap client里的datasource 作为默认数据源
        if (defaultDataSource == null) {
            defaultDataSource = sqlMapClient.getDataSource();
            String identity = getDefaultDataSourceName();
            CobarDataSourceDescriptor descriptor = new CobarDataSourceDescriptor();
            descriptor.setIdentity(identity);
            descriptor.setPoolSize(Runtime.getRuntime().availableProcessors() * 5);
            descriptor.setTargetDataSource(defaultDataSource);

            defaultDataSourceDescriptor = descriptor;

            getDataSources().put(identity, defaultDataSource);

            getDataSourceDescriptors().add(descriptor);
        }

        // 判断是否有值
        Assert.notNull(defaultDataSource);
        Assert.notNull(defaultDataSourceDescriptor);

    }

    public DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public CobarDataSourceDescriptor getDefaultDataSourceDescriptor() {
        return defaultDataSourceDescriptor;
    }

    public void setDefaultDataSourceDescriptor(CobarDataSourceDescriptor defaultDataSourceDescriptor) {
        this.defaultDataSourceDescriptor = defaultDataSourceDescriptor;
    }

    public boolean contains(String dataSourceId) {
        return getDataSources().containsKey(dataSourceId);
    }

    public SortedMap<String, DataSource> getDataSourses(List<String> dataSourceIdList, boolean sort) {

        SortedMap<String, DataSource> resultMap = null;

        if (CollectionUtils.isNotEmpty(dataSourceIdList)) {

            resultMap = new TreeMap<String, DataSource>();
            
            if (sort) {
                Collections.sort(dataSourceIdList);
            }

            for (String dsName : dataSourceIdList) {
                resultMap.put(dsName, getDataSources().get(dsName));
            }
        }

        return resultMap;
    }

    public DataSource getDataSource(String dataSourceId) {
        return getDataSources().get(dataSourceId);
    }

    public SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }

    public void setDefaultDataSourceName(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
    }

}
