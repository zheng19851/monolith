package com.runssnail.monolith.dal.datasources;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;

import com.alibaba.cobar.client.datasources.CobarDataSourceDescriptor;
import com.alibaba.cobar.client.datasources.DefaultCobarDataSourceService;
import com.alibaba.cobar.client.datasources.ha.NonHADataSourceCreator;
import com.alibaba.cobar.client.support.utils.CollectionUtils;

/**
 * 默认的数据源管理实现
 * 
 * @author zhengwei
 */
public class DefaultMonoDataSourceService extends DefaultCobarDataSourceService implements MonoDataSourceService {

    // using MonoSqlMapClientTemplate's getDataSource() methed to fetch the default dataSource
    // private MonoDataSourceDescriptor defaultDataSourceDescriptor;

    public void afterPropertiesSet() throws Exception {

        if (getHaDataSourceCreator() == null) {
            setHaDataSourceCreator(new NonHADataSourceCreator());
        }
        if (CollectionUtils.isEmpty(getDataSourceDescriptors())) {
            return;
        }

        for (CobarDataSourceDescriptor descriptor : getDataSourceDescriptors()) {

            // MonoDataSourceDescriptor monoDescriptor = (MonoDataSourceDescriptor) descriptor;

            Validate.notEmpty(descriptor.getIdentity());
            Validate.notNull(descriptor.getTargetDataSource());

            DataSource dataSourceToUse = descriptor.getTargetDataSource();

            if (descriptor.getStandbyDataSource() != null) {
                dataSourceToUse = getHaDataSourceCreator().createHADataSource(descriptor);
            }

            // DataSource proxyDataSource = new LazyConnectionDataSourceProxy(dataSourceToUse);
            // getDataSources().put(descriptor.getIdentity(), proxyDataSource);

            getDataSources().put(descriptor.getIdentity(), dataSourceToUse);
            // if (monoDescriptor.isDefaultDataSource()) {
            // this.defaultDataSourceDescriptor = monoDescriptor;
            // }
        }

        // Assert.notNull(this.defaultDataSourceDescriptor, "Has not set the default datasource.");

    }

    // public DataSource getDefaultDataSource() {
    // return this.defaultDataSourceDescriptor.getTargetDataSource();
    // }
    //
    // public MonoDataSourceDescriptor getDefaultDataSourceDescriptor() {
    // return this.defaultDataSourceDescriptor;
    // }
    //
    // public void setDefaultDataSourceDescriptor(MonoDataSourceDescriptor defaultDataSourceDescriptor) {
    // this.defaultDataSourceDescriptor = defaultDataSourceDescriptor;
    // }

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

}
