package com.kongur.monolith.dal.router;

import com.alibaba.cobar.client.router.RoutingException;
import com.kongur.monolith.dal.router.support.RoutingResult;

/**
 * 
 * @author zhengwei
 *
 * @param <T>
 */
public interface Router<T> {
    
    /**
     * ·Ö¿â·Ö±í
     * 
     * @param routingFact
     * @return
     * @throws RoutingException
     */
    RoutingResult doRoute(T routingFact) throws RoutingException;
    
}
