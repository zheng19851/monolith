package com.kongur.monolith.dal.route;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.log4j.Logger;

import com.kongur.monolith.dal.datasource.DataSourceManager;
import com.kongur.monolith.dal.route.conf.RouteRulesManager;
import com.kongur.monolith.dal.route.rule.Rule;

/**
 * 默认的数据源路由器
 * 
 * @author zhengwei
 */
public class DefaultDataSourceRouter implements DataSourceRouter {

    private final Logger      log = Logger.getLogger(getClass());

    /**
     * 路由规则管理器
     */
    private RouteRulesManager routeRulesManager;

    /**
     * 数据源管理
     */
    private DataSourceManager dataSourceManager;

    @Override
    public DataSource routeDataSource(MappedStatement ms, Object paramObj) {

        return this.routeDataSource(ms.getId(), paramObj);
    }

    @Override
    public DataSource routeDataSource(String statementId, Object paramObj) {
        if (log.isInfoEnabled()) {
            log.info("routeDataSource start, statementId=" + statementId + ", paramObj=" + paramObj);
        }
        String dataSourceId = getDataSourceId(statementId, paramObj);

        DataSource dataSource = null;
        if (StringUtils.isNotBlank(dataSourceId)) {
            dataSource = dataSourceManager.getDataSource(dataSourceId);
            if (dataSource == null) {
                throw new RuntimeException("can not route DataSource, the DataSource is not exists, dataSourceId="
                                           + dataSourceId);
            }

        }

        if (log.isInfoEnabled()) {
            log.info("routeDataSource " + (dataSource != null ? "success, dataSourceId=" + dataSourceId : "fail")
                     + ", statementId=" + statementId + ", paramObj=" + paramObj);
        }

        return dataSource;
    }

    private String getDataSourceId(String statementId, Object paramObj) {
        List<Rule> rules = routeRulesManager.getRules();
        for (Rule rule : rules) {
            if (rule.isMatch(statementId, paramObj)) {
                return rule.getDataSourceId();
            }
        }

        return null;
    }

    public RouteRulesManager getRouteRulesManager() {
        return routeRulesManager;
    }

    public void setRouteRulesManager(RouteRulesManager routeRulesManager) {
        this.routeRulesManager = routeRulesManager;
    }

    public DataSourceManager getDataSourceManager() {
        return dataSourceManager;
    }

    public void setDataSourceManager(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

}
