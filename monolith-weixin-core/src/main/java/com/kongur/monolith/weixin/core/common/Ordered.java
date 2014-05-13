package com.kongur.monolith.weixin.core.common;

/**
 * 需要被排序的对象可以实现此接口
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public interface Ordered {

    /**
     * 最高优先级
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * 最低优先级
     */
    int LOWEST_PRECEDENCE  = Integer.MAX_VALUE;

    /**
     * 排序值
     * 
     * @return
     */
    int getOrder();

}
