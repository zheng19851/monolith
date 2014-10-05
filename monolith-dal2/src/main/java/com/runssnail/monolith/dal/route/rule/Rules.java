package com.runssnail.monolith.dal.route.rule;

import java.util.List;

import com.runssnail.monolith.common.DomainBase;

public class Rules extends DomainBase {

    /**
     * 
     */
    private static final long serialVersionUID = -5577164163707017798L;
    private List<DefaultRule> rules;

    public List<DefaultRule> getRules() {
        return rules;
    }

    public void setRules(List<DefaultRule> rules) {
        this.rules = rules;
    }

}
