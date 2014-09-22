package com.kongur.monolith.dal.route;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 数据源路由器
 * 
 * @author zhengwei
 */
public interface DataSourceRouter {

    /**
     * 根据MappedStatement 和 paramObj确定哪个DataSource
     * 
     * @param ms sql配置信息
     * @param paramObj 参数
     * @return
     */
    DataSource routeDataSource(MappedStatement ms, Object paramObj);

    /**
     * 根据statementId 和 paramObj确定哪个DataSource
     * 
     * @param statementId sql mapper文件里的sql的id
     * @param paramObj 参数
     * @return
     */
    DataSource routeDataSource(String statementId, Object paramObj);

}
