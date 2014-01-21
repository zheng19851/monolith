package com.kongur.monolith.dal.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.cobar.client.router.RoutingException;
import com.alibaba.cobar.client.router.rules.IRoutingRule;
import com.alibaba.cobar.client.router.support.IBatisRoutingFact;
import com.alibaba.cobar.client.support.LRUMap;
import com.alibaba.cobar.client.support.utils.CollectionUtils;
import com.kongur.monolith.dal.router.rules.ibatis.AbstractMonoIBatisOrientedRule;
import com.kongur.monolith.dal.router.support.RoutingResult;

/**
 * 路由
 * 
 * @author zhengwei
 */
public class DefaultRouter implements Router<IBatisRoutingFact> {

    private transient final Logger logger      = LoggerFactory.getLogger(DefaultRouter.class);

    /**
     * 本地缓存
     */
    private LRUMap                 localCache;

    /**
     * 是否打开缓存
     */
    private boolean                enableCache = false;

    public DefaultRouter(boolean enableCache) {
        this(enableCache, 10000);
    }

    public DefaultRouter(int cacheSize) {
        this(true, cacheSize);
    }

    public DefaultRouter(boolean enableCache, int cacheSize) {
        this.enableCache = enableCache;
        if (this.enableCache) {
            localCache = new LRUMap(cacheSize);
        }
    }

    /**
     * 路由规则定义
     */
    private List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequences = new ArrayList<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>();

    public RoutingResult doRoute(IBatisRoutingFact routingFact) throws RoutingException {
        if (enableCache) {
            synchronized (localCache) {
                if (localCache.containsKey(routingFact)) {
                    RoutingResult result = (RoutingResult) localCache.get(routingFact);
                    logger.info("return routing result:{} from cache for fact:{}", result, routingFact);
                    return result;
                }
            }
        }

        RoutingResult result = new RoutingResult();

        // datasource ids
        result.setResourceIdentities(new ArrayList<String>());

        IRoutingRule<IBatisRoutingFact, List<String>> ruleToUse = null;

        if (!CollectionUtils.isEmpty(getRuleSequences())) {
            for (Set<IRoutingRule<IBatisRoutingFact, List<String>>> ruleSet : getRuleSequences()) {
                ruleToUse = searchMatchedRuleAgainst(ruleSet, routingFact);
                if (ruleToUse != null) {
                    break;
                }
            }
        }

        if (ruleToUse != null) {
            logger.info("matched with rule:{} with fact:{}", ruleToUse, routingFact);
            result.addResourceIdentities(ruleToUse.action()); // set the resolved datasources

            AbstractMonoIBatisOrientedRule useRule = (AbstractMonoIBatisOrientedRule) ruleToUse;

            String tableSuffix = useRule.resolveTableSuffix(routingFact);
            result.setTableSuffix(tableSuffix);
        } else {
            logger.info("No matched rule found for routing fact:{}", routingFact);
        }

        if (enableCache) {
            synchronized (localCache) {
                localCache.put(routingFact, result);
            }
        }

        return result;
    }

    private IRoutingRule<IBatisRoutingFact, List<String>> searchMatchedRuleAgainst(Set<IRoutingRule<IBatisRoutingFact, List<String>>> rules,
                                                                                   IBatisRoutingFact routingFact) {
        if (CollectionUtils.isEmpty(rules)) {
            return null;
        }

        for (IRoutingRule<IBatisRoutingFact, List<String>> rule : rules) {
            if (rule.isDefinedAt(routingFact)) {
                return rule;
            }
        }

        logger.warn("can not find rule, routingFact=" + routingFact);

        return null;
    }

    public void setLocalCache(LRUMap localCache) {
        this.localCache = localCache;
    }

    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    public LRUMap getLocalCache() {
        return localCache;
    }

    public synchronized void clearLocalCache() {
        this.localCache.clear();
    }

    public boolean isEnableCache() {
        return enableCache;
    }

    public void setRuleSequences(List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequences) {
        this.ruleSequences = ruleSequences;
    }

    public List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> getRuleSequences() {
        return ruleSequences;
    }
}
