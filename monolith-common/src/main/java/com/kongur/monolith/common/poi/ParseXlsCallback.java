package com.kongur.monolith.common.poi;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.kongur.monolith.common.result.Result;

/**
 * 解析xls回调接口
 * 
 * @author zhengwei
 *
 * @param <E>
 */
public interface ParseXlsCallback<E> extends ParseCallback<E> {
    
    /**
     * 填充数据对象
     * 
     * @param obj
     * @param value
     * @param cellNum 从0开始
     * @param result TODO
     */
    void fillObj(E obj, HSSFCell cell, String value, int cellNum, Result<List<E>> result);

}
