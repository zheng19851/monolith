package com.kongur.monolith.dal.router.rules.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import com.alibaba.cobar.client.router.support.IBatisRoutingFact;

public class MonoIBatisNamespaceShardingRule extends AbstractMonoIBatisOrientedRule {

    public MonoIBatisNamespaceShardingRule(String pattern, String action, String attributePattern) {
        super(pattern, action, attributePattern);
    }

    public boolean isDefinedAt(IBatisRoutingFact routingFact) {
        Validate.notNull(routingFact);
        String namespace = StringUtils.substringBeforeLast(routingFact.getAction(), ".");
        boolean matches = StringUtils.equals(namespace, getTypePattern());
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
        return "IBatisNamespaceShardingRule [getAttributePattern()=" + getAttributePattern() + ", getAction()="
               + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
    }

}
