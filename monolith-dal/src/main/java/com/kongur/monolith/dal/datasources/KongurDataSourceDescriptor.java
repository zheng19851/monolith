package com.kongur.monolith.dal.datasources;

import com.alibaba.cobar.client.datasources.CobarDataSourceDescriptor;

/**
 * @author zhengwei
 */
public class KongurDataSourceDescriptor extends CobarDataSourceDescriptor {

    /**
     * 是否是默认的数据源
     */
    private boolean defaultDataSource;

    public boolean isDefaultDataSource() {
        return defaultDataSource;
    }

    public boolean getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(boolean defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }
}
