package com.runssnail.monolith.dal.router.rules.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import com.alibaba.cobar.client.router.support.IBatisRoutingFact;

/**
 * @author zhengwei
 */
public class MonoIBatisSqlActionShardingRule extends AbstractMonoIBatisOrientedRule {

    public MonoIBatisSqlActionShardingRule(String pattern, String action, String attributePattern) {
        super(pattern, action, attributePattern);
    }

    public boolean isDefinedAt(IBatisRoutingFact routingFact) {
        Validate.notNull(routingFact);
        boolean matches = StringUtils.equals(getTypePattern(), routingFact.getAction());
        if (matches) {
            try {
                Map<String, Object> vrs = new HashMap<String, Object>();
                vrs.putAll(getFunctionMap());
                vrs.put("$ROOT", routingFact.getArgument()); // add top object reference for expression
                VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);
                if (MVEL.evalToBoolean(getAttributePattern(), routingFact.getArgument(), vrfactory)) {
                    return true;
                }
            } catch (Throwable t) {
                logger.info("failed to evaluate attribute expression:'{}' with context object:'{}'\n{}", new Object[] {
                        getAttributePattern(), routingFact.getArgument(), t });
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "KongurIBatisSqlActionShardingRule [getAttributePattern()=" + getAttributePattern() + ", getAction()="
               + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
    }

}
