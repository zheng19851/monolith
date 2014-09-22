package com.kongur.monolith.dal.datasource;

import javax.sql.DataSource;

/**
 * 数据源管理
 * 
 * @author zhengwei
 *
 */
public interface DataSourceManager {

    /**
     * 获取数据源
     * 
     * @param dataSourceId 数据源id
     * @return
     */
    DataSource getDataSource(String dataSourceId);
    
}
