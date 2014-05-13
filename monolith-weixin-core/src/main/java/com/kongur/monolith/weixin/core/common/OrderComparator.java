package com.kongur.monolith.weixin.core.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 排序实现了Ordered接口的对象
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class OrderComparator implements Comparator<Object> {

    public static OrderComparator INSTANCE = new OrderComparator();

    @Override
    public int compare(Object o1, Object o2) {

        int i1 = getOrder(o1);
        int i2 = getOrder(o2);

        return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
    }
    
    
    protected int getOrder(Object obj) {
        return (obj instanceof Ordered ? ((Ordered) obj).getOrder() : Ordered.LOWEST_PRECEDENCE);
    }

    /**
     * 对列表排序
     * 
     * @param list
     */
    public static void sort(List<?> list) {
        if (list.size() > 1) {
            Collections.sort(list, INSTANCE);
        }
    }

    /**
     * 对数组排序
     * 
     * @param array
     */
    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }
    }
}
