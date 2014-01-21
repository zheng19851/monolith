package com.kongur.monolith.dal.router;

import com.alibaba.cobar.client.router.RoutingException;
import com.kongur.monolith.dal.router.support.RoutingResult;

/**
 * 数据源和表名路由
 * 
 * @author zhengwei
 * @param <T>
 */
public interface Router<T> {

    /**
     * 分库分表
     * 
     * @param routingFact
     * @return
     * @throws RoutingException
     */
    RoutingResult doRoute(T routingFact) throws RoutingException;

}
