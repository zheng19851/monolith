package com.runssnail.monolith.dal.datasource;

import javax.sql.DataSource;

/**
 * 热备数据源创建器
 * 
 * @author zhengwei
 *
 */
public interface HADataSourceCreator {

    DataSource createHADataSource(DataSourceDescriptor descriptor);

}
