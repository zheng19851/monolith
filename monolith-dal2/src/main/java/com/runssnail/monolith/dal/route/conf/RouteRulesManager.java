package com.runssnail.monolith.dal.route.conf;

import java.util.List;

import com.runssnail.monolith.dal.route.rule.Rule;

/**
 * 路由规则管理
 * 
 * @author zhengwei
 *
 */
public interface RouteRulesManager {

    List<Rule> getRules();
}
