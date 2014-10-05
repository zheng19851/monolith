package com.runssnail.monolith.dal.route.rule;

import java.util.Map;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 有配置命名空间规则定义
 * 
 * @author zhengwei
 *
 */
public class NamespaceRule extends AbstractRule {

    /**
     * 
     */
    private static final long serialVersionUID = -8602739520279769623L;

    /**
     * 命名空间
     */
    private String            namespace;

    public NamespaceRule(Map<String, Object> functions) {
        super(functions);
    }

    public NamespaceRule(String namespace, String expression, String dataSourceId, Map<String, Object> functions) {
        super(expression, dataSourceId, functions);
        this.namespace = namespace;
    }

    @Override
    public boolean isMatch(MappedStatement ms, Object paramObj) {
        return isMatch(ms.getId(), paramObj);
    }

    @Override
    public boolean isMatch(String statementId, Object paramObj) {
        String namespace = getNamespace(statementId);
        return this.namespace.equals(namespace) && executeExpression(paramObj);
    }

    /**
     * 获取命名空间
     * 
     * @param ms
     * @return
     */
    private String getNamespace(String statementId) {

        String namespace = statementId.substring(0, statementId.lastIndexOf("."));
        return namespace;
    }

}
