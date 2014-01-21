package com.kongur.monolith.dal.datasources;

import com.alibaba.cobar.client.datasources.CobarDataSourceDescriptor;

/**
 * 数据源描述
 * 
 * @author zhengwei
 */
public class MonoDataSourceDescriptor extends CobarDataSourceDescriptor {

    /**
     * 探测sql，探测数据源是否可用
     */
    private String detectingSql = "select 1 from dual";

    public String getDetectingSql() {
        return detectingSql;
    }

    public void setDetectingSql(String detectingSql) {
        this.detectingSql = detectingSql;
    }

}
