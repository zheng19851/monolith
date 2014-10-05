package com.runssnail.monolith.dal.route.rule;

import java.util.Map;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 路由规则
 * 
 * @author zhengwei
 */
public interface Rule {

    /**
     * 是否匹配
     * 
     * @param ms sql配置
     * @param paramObj 执行sql时的参数
     * @return
     */
    boolean isMatch(MappedStatement ms, Object paramObj);
    
    /**
     * 是否匹配
     * 
     * @param statementId mybatis配置文件里sql配置id
     * @param paramObj 参数
     * @return
     */
    boolean isMatch(String statementId, Object paramObj);

    /**
     * 数据源id
     * 
     * @return
     */
    String getDataSourceId();
    
    public Map<String, Object> getFunctions();

    public void setFunctions(Map<String, Object> functions);

}
