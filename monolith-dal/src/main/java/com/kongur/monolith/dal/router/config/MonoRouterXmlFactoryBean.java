package com.kongur.monolith.dal.router.config;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.io.Resource;

import com.alibaba.cobar.client.router.rules.IRoutingRule;
import com.alibaba.cobar.client.router.support.IBatisRoutingFact;
import com.alibaba.cobar.client.support.utils.CollectionUtils;
import com.alibaba.cobar.client.support.utils.MapUtils;
import com.kongur.monolith.dal.router.DefaultRouter;
import com.kongur.monolith.dal.router.config.vo.MonoInternalRule;
import com.kongur.monolith.dal.router.config.vo.MonoInternalRules;
import com.kongur.monolith.dal.router.rules.ibatis.MonoIBatisNamespaceRule;
import com.kongur.monolith.dal.router.rules.ibatis.MonoIBatisNamespaceShardingRule;
import com.kongur.monolith.dal.router.rules.ibatis.MonoIBatisSqlActionRule;
import com.kongur.monolith.dal.router.rules.ibatis.MonoIBatisSqlActionShardingRule;
import com.thoughtworks.xstream.XStream;

/**
 * ´´½¨DefaultRouter
 * 
 * @author zhengwei
 */
public class MonoRouterXmlFactoryBean extends AbstractMonoRouterConfigurationFactoryBean {

    @Override
    protected void assembleRulesForRouter(DefaultRouter router, Resource configLocation,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionShardingRules,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionRules,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceShardingRules,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceRules)
                                                                                                            throws IOException {
        XStream xstream = new XStream();
        xstream.alias("rules", MonoInternalRules.class);
        xstream.alias("rule", MonoInternalRule.class);
        xstream.addImplicitCollection(MonoInternalRules.class, "rules");
        xstream.useAttributeFor(MonoInternalRule.class, "merger");

        MonoInternalRules internalRules = (MonoInternalRules) xstream.fromXML(configLocation.getInputStream());
        List<MonoInternalRule> rules = internalRules.getRules();
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }

        for (MonoInternalRule rule : rules) {

            String namespace = StringUtils.trimToEmpty(rule.getNamespace());
            String sqlAction = StringUtils.trimToEmpty(rule.getSqlmap());
            String shardingExpression = StringUtils.trimToEmpty(rule.getShardingExpression());
            String destinations = StringUtils.trimToEmpty(rule.getShards());
            String tableResolveExp = StringUtils.trimToEmpty(rule.getTableResolveExp());

            Validate.notEmpty(destinations, "destination shards must be given explicitly.");

            if (StringUtils.isEmpty(namespace) && StringUtils.isEmpty(sqlAction)) {
                throw new IllegalArgumentException("at least one of 'namespace' or 'sqlAction' must be given.");
            }
            if (StringUtils.isNotEmpty(namespace) && StringUtils.isNotEmpty(sqlAction)) {
                throw new IllegalArgumentException(
                                                   "'namespace' and 'sqlAction' are alternatives, can't guess which one to use if both of them are provided.");
            }

            if (StringUtils.isNotEmpty(namespace)) {
                if (StringUtils.isEmpty(shardingExpression)) {
                    MonoIBatisNamespaceRule namespaceRule = new MonoIBatisNamespaceRule(namespace, destinations);
                    namespaceRule.setTableResolveExp(tableResolveExp);
                    namespaceRule.setFunctionMap(getFunctionsMap());
                    namespaceRules.add(namespaceRule);
                } else {
                    MonoIBatisNamespaceShardingRule insr = new MonoIBatisNamespaceShardingRule(namespace, destinations,
                                                                                       shardingExpression);
                    if (MapUtils.isNotEmpty(getFunctionsMap())) {
                        insr.setFunctionMap(getFunctionsMap());
                    }

                    insr.setTableResolveExp(tableResolveExp);

                    namespaceShardingRules.add(insr);
                }
            }
            if (StringUtils.isNotEmpty(sqlAction)) {
                if (StringUtils.isEmpty(shardingExpression)) {
                    MonoIBatisSqlActionRule sqlActionRule = new MonoIBatisSqlActionRule(sqlAction, destinations);
                    sqlActionRule.setTableResolveExp(tableResolveExp);
                    sqlActionRule.setFunctionMap(getFunctionsMap());
                    sqlActionRules.add(sqlActionRule);
                } else {
                    MonoIBatisSqlActionShardingRule issr = new MonoIBatisSqlActionShardingRule(sqlAction, destinations,
                                                                                       shardingExpression);
                    if (MapUtils.isNotEmpty(getFunctionsMap())) {
                        issr.setFunctionMap(getFunctionsMap());
                    }
                    issr.setTableResolveExp(tableResolveExp);
                    sqlActionShardingRules.add(issr);
                }
            }
        }

    }
}
