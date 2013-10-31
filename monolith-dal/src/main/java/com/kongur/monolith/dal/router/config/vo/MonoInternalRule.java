package com.kongur.monolith.dal.router.config.vo;


/**
 * @author zhengwei
 */
public class MonoInternalRule {

    private String namespace;
    private String sqlmap;
    private String shardingExpression;
    private String shards;
    /**
     * this field is not used for now, because it's still in leverage whether it's proper to bind merging information
     * into a routing concern.
     */
    private String merger;

    private String tableResolveExp;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSqlmap() {
        return sqlmap;
    }

    public void setSqlmap(String sqlmap) {
        this.sqlmap = sqlmap;
    }

    public String getShardingExpression() {
        return shardingExpression;
    }

    public void setShardingExpression(String shardingExpression) {
        this.shardingExpression = shardingExpression;
    }

    public String getShards() {
        return shards;
    }

    public void setShards(String shards) {
        this.shards = shards;
    }

    public String getMerger() {
        return merger;
    }

    public void setMerger(String merger) {
        this.merger = merger;
    }

    public String getTableResolveExp() {
        return tableResolveExp;
    }

    public void setTableResolveExp(String tableResolveExp) {
        this.tableResolveExp = tableResolveExp;
    }

}
