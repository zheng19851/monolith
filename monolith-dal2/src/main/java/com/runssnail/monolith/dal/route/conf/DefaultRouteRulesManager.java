package com.runssnail.monolith.dal.route.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.runssnail.monolith.dal.route.rule.DefaultRule;
import com.runssnail.monolith.dal.route.rule.NamespaceRule;
import com.runssnail.monolith.dal.route.rule.Rule;
import com.runssnail.monolith.dal.route.rule.RuleException;
import com.runssnail.monolith.dal.route.rule.Rules;
import com.runssnail.monolith.dal.route.rule.SqlIdRule;
import com.thoughtworks.xstream.XStream;

/**
 * 默认路由规则管理器实现
 * 
 * @author zhengwei
 */
public class DefaultRouteRulesManager implements RouteRulesManager, InitializingBean {

    private final Logger        log = Logger.getLogger(getClass());

    /**
     * 路由配置文件路径
     */
    private Resource            configLocation;

    private XStream             xStream;

    private List<Rule>          rules;

    private Map<String, Object> functions;

    public Map<String, Object> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, Object> functions) {
        this.functions = functions;
    }

    public Resource getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.xStream == null) {
            XStream xStream = new XStream();
            xStream.alias("rules", Rules.class);
            xStream.alias("rule", DefaultRule.class);
            xStream.addImplicitCollection(Rules.class, "rules");

            this.xStream = xStream;
        }

        Rules rules = (Rules) this.xStream.fromXML(this.configLocation.getInputStream());
        List<DefaultRule> defaultRuleList = rules.getRules();

        List<Rule> ruleList = new ArrayList<Rule>(defaultRuleList.size());

        for (DefaultRule defaultRule : defaultRuleList) {
            Rule rule = null;
            if (StringUtils.isNotBlank(defaultRule.getSqlId())) {
                 rule = new SqlIdRule(defaultRule.getSqlId(), defaultRule.getExpression(), defaultRule.getDataSourceId(), this.functions);
            } else if (StringUtils.isNotBlank(defaultRule.getNamespace())) {
                 rule = new NamespaceRule(defaultRule.getNamespace(), defaultRule.getExpression(), defaultRule.getDataSourceId(), this.functions);
            } else {
                throw new RuleException("the rule is error, the ('sqlId' or 'namespace') and 'dataSourceId' must be set. rule=" + defaultRule);
            }
            
            if(StringUtils.isBlank(rule.getDataSourceId())) {
                throw new RuleException("the rule is error, the 'dataSourceId' must be set. rule=" + defaultRule);
            }
            ruleList.add(rule);
        }
        
        this.rules = ruleList;

        if (log.isInfoEnabled()) {
            log.info("read rules successful, rules=" + ruleList);
        }

    }

    @Override
    public List<Rule> getRules() {
        return this.rules;
    }

}
