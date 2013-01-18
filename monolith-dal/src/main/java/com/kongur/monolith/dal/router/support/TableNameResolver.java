package com.kongur.monolith.dal.router.support;

/**
 * 决定分表名
 * 
 * @author zhengwei
 *
 */
public interface TableNameResolver<F> {
    
    String resolveTableSuffix(F routingFact);
    
}
