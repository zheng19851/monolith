package com.kongur.monolith.dal.route.rule;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import com.kongur.monolith.common.DomainBase;

/**
 * 路由规则抽象
 * 
 * @author zhengwei
 */
public abstract class AbstractRule extends DomainBase implements Rule {

    /**
     * 
     */
    private static final long     serialVersionUID = 1257134630287061802L;

    protected final Logger        logger           = Logger.getLogger(getClass());

    protected Map<String, Object> functions;

    /**
     * 表达式
     */
    protected String              expression;

    /**
     * 数据源id
     */
    protected String              dataSourceId;

    public AbstractRule(Map<String, Object> functions) {
        this.functions = functions;
    }

    public AbstractRule(String expression, String dataSourceId, Map<String, Object> functions) {
        this.expression = expression;
        this.dataSourceId = dataSourceId;
        this.functions = functions;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, Object> functions) {
        this.functions = functions;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Override
    public String getDataSourceId() {
        return this.dataSourceId;
    }

    /**
     * 执行表达式
     * 
     * @param paramObj 参数
     * @return
     */
    protected boolean executeExpression(Object paramObj) {
        if (StringUtils.isBlank(this.expression)) {
            return true;
        }

        try {
            Map<String, Object> vrs = new HashMap<String, Object>();
            vrs.putAll(getFunctions());
            vrs.put("$ROOT", paramObj); // add top object reference for expression
            VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);
            if (MVEL.evalToBoolean(this.expression, paramObj, vrfactory)) {
                return true;
            }
        } catch (Throwable t) {
            logger.error("failed to evaluate expression:'" + this.expression + "' with context object:'" + paramObj, t);
        }
        return false;
    }

}
