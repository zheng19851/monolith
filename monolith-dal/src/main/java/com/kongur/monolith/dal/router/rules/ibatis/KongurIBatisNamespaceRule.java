package com.kongur.monolith.dal.router.rules.ibatis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.alibaba.cobar.client.router.support.IBatisRoutingFact;

/**
 * @author zhengwei
 */
public class KongurIBatisNamespaceRule extends AbstractKongurIBatisOrientedRule {

    public KongurIBatisNamespaceRule(String pattern, String action) {
        super(pattern, action);
    }

    public boolean isDefinedAt(IBatisRoutingFact routingFact) {
        Validate.notNull(routingFact);
        String namespace = StringUtils.substringBeforeLast(routingFact.getAction(), ".");
        return StringUtils.equals(namespace, getTypePattern());
    }

    @Override
    public String toString() {
        return "KongurIBatisNamespaceRule [getAction()=" + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
    }
}
