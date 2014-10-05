package com.runssnail.monolith.dal.route.rule;

import com.runssnail.monolith.common.DomainBase;

/**
 * 规则定义，方便xml解析
 * 
 * @author zhengwei
 */
public class DefaultRule extends DomainBase {

    /**
     * 
     */
    private static final long serialVersionUID = 1426502807328051931L;

    /**
     * mybaits mapper文件里配置的sql id
     */
    private String            sqlId;

    /**
     * 命名空间
     */
    private String            namespace;

    /**
     * 表达式
     */
    private String            expression;

    /**
     * 数据源id
     */
    private String            dataSourceId;

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

}
