package com.runssnail.monolith.common.poi;

/**
 * 解析excel回调接口
 * 
 * @author zhengwei
 */
public interface ParseCallback<E> {

    /**
     * 创建对象(一行表示一个数据对象)
     * 
     * @return
     */
    public E createObj();

    /**
     * 对象是否有效，有效才会加入到有效列表里，并返回
     * 
     * @param e
     * @return
     */
    boolean isValid(E e);

}
