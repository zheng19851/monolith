package com.kongur.monolith.dal.router.rules.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.slf4j.LoggerFactory;

import com.alibaba.cobar.client.router.rules.ibatis.AbstractIBatisOrientedRule;
import com.alibaba.cobar.client.router.support.IBatisRoutingFact;

/**
 * @author zhengwei
 */
public abstract class AbstractMonoIBatisOrientedRule extends AbstractIBatisOrientedRule {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 决定表名称的
     */
    private String                   tableResolveExp;

    public AbstractMonoIBatisOrientedRule(String pattern, String action) {
        super(pattern, action);
    }

    public AbstractMonoIBatisOrientedRule(String pattern, String action, String attributePattern) {
        super(pattern, action, attributePattern);
    }

    public String getTableResolveExp() {
        return tableResolveExp;
    }

    public void setTableResolveExp(String tableResolveExp) {
        this.tableResolveExp = tableResolveExp;
    }

    public String resolveTableSuffix(IBatisRoutingFact routingFact) {

        if (StringUtils.isBlank(tableResolveExp)) {
            return "";
        }

        try {
            Map<String, Object> vrs = new HashMap<String, Object>();
            vrs.putAll(getFunctionMap());
            vrs.put("$ROOT", routingFact.getArgument()); // add top object reference for expression
            VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);

            return MVEL.evalToString(tableResolveExp, routingFact.getArgument(), vrfactory);

        } catch (Throwable t) {
            logger.info("failed to evaluate attribute expression:'{}' with context object:'{}'\n{}", new Object[] {
                    tableResolveExp, routingFact.getArgument(), t });
        }

        return null;
    }

}
