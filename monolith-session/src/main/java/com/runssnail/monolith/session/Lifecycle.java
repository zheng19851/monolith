package com.runssnail.monolith.session;

/**
 * 生命周期接口
 * 
 * @author zhengwei
 */
public interface Lifecycle {

    /**
     * 初始化
     */
    void init();

    /**
     * 销毁
     */
    void destroy();

}
