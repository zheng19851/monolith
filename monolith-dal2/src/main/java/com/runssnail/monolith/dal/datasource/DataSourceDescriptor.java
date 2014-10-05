package com.runssnail.monolith.dal.datasource;

import javax.sql.DataSource;

/**
 * 数据源描述
 * 
 * @author zhengwei
 */
public class DataSourceDescriptor {

    /**
     * 数据源id
     */
    private String     identity;

    /**
     * 主数据源
     */
    private DataSource masterDataSource;

    /**
     * 探测用的主数据源配置
     */
    private DataSource masterDetectorDataSource;

    /**
     * 备库
     */
    private DataSource slaveDataSource;

    /**
     * 探测用的备库源配置
     */
    private DataSource slaveDetectorDataSource;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * 主库
     * 
     * @return
     */
    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public DataSource getMasterDetectorDataSource() {
        return masterDetectorDataSource;
    }

    public void setMasterDetectorDataSource(DataSource masterDetectorDataSource) {
        this.masterDetectorDataSource = masterDetectorDataSource;
    }

    public DataSource getSlaveDataSource() {
        return slaveDataSource;
    }

    public void setSlaveDataSource(DataSource slaveDataSource) {
        this.slaveDataSource = slaveDataSource;
    }

    public DataSource getSlaveDetectorDataSource() {
        return slaveDetectorDataSource;
    }

    public void setSlaveDetectorDataSource(DataSource slaveDetectorDataSource) {
        this.slaveDetectorDataSource = slaveDetectorDataSource;
    }

}
