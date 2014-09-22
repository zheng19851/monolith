package com.kongur.monolith.dal.datasource;

import javax.sql.DataSource;

/**
 * д╛хой╣ож
 * 
 * @author zhengwei
 *
 */
public class NonHADataSourceCreator implements HADataSourceCreator {

    @Override
    public DataSource createHADataSource(DataSourceDescriptor descriptor) {
        return descriptor.getMasterDataSource();
    }

}
