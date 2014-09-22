package com.kongur.monolith.dal.datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.util.CollectionUtils;

/**
 * 默认的{@link DataSourceManager}
 * 
 * @author zhengwei
 */
public class DefaultDataSourceManager implements DataSourceManager, InitializingBean {

    protected final Logger            log                   = Logger.getLogger(getClass());

    /**
     * 配置
     */
    private Set<DataSourceDescriptor> dataSourceDescriptors = null;

    /**
     * 使用的数据源
     */
    private Map<String, DataSource>   dataSources           = null;

    private HADataSourceCreator       haDataSourceCreator;

    /**
     * 是否延迟
     */
    private boolean                   lazyConnection        = false;

    @Override
    public DataSource getDataSource(String dataSourceId) {
        return dataSources.get(dataSourceId);

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (this.haDataSourceCreator == null) {
            this.haDataSourceCreator = new NonHADataSourceCreator();
        }

        if (CollectionUtils.isEmpty(dataSourceDescriptors)) {
            return;
        }

        this.dataSources = new HashMap<String, DataSource>(dataSourceDescriptors.size());

        for (DataSourceDescriptor descriptor : this.dataSourceDescriptors) {
            Validate.notEmpty(descriptor.getIdentity());
            Validate.notNull(descriptor.getMasterDataSource());

            DataSource dataSourceToUse = descriptor.getMasterDataSource();

            if (descriptor.getSlaveDataSource() != null) {
                dataSourceToUse = this.haDataSourceCreator.createHADataSource(descriptor);
            }

            if (lazyConnection) {
                dataSourceToUse = new LazyConnectionDataSourceProxy(dataSourceToUse);
            }
            dataSources.put(descriptor.getIdentity(), dataSourceToUse);
        }

    }

    public boolean isLazyConnection() {
        return lazyConnection;
    }

    public void setLazyConnection(boolean lazyConnection) {
        this.lazyConnection = lazyConnection;
    }

    public Set<DataSourceDescriptor> getDataSourceDescriptors() {
        return dataSourceDescriptors;
    }

    public void setDataSourceDescriptors(Set<DataSourceDescriptor> dataSourceDescriptors) {
        this.dataSourceDescriptors = dataSourceDescriptors;
    }

    public HADataSourceCreator getHaDataSourceCreator() {
        return haDataSourceCreator;
    }

    public void setHaDataSourceCreator(HADataSourceCreator haDataSourceCreator) {
        this.haDataSourceCreator = haDataSourceCreator;
    }

}
