package com.runssnail.monolith.dal.support.domain;

import java.io.Serializable;

/**
 * 
 * @author zhengwei
 *
 */
public interface Routingable extends Serializable {
    
    void setTableSuffix(String tableSuffix);

    String getTableSuffix();
    
}
