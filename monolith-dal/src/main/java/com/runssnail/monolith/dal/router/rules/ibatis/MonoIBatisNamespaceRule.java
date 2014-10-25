package com.runssnail.monolith.dal.router.rules.ibatis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.alibaba.cobar.client.router.support.IBatisRoutingFact;

/**
 * @author zhengwei
 */
public class MonoIBatisNamespaceRule extends AbstractMonoIBatisOrientedRule {

    public MonoIBatisNamespaceRule(String pattern, String action) {
        super(pattern, action);
    }

    public boolean isDefinedAt(IBatisRoutingFact routingFact) {
        Validate.notNull(routingFact);
        String namespace = StringUtils.substringBeforeLast(routingFact.getAction(), ".");
        return StringUtils.equals(namespace, getTypePattern());
    }

    @Override
    public String toString() {
        return "MonoIBatisNamespaceRule [getAction()=" + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
    }
}
