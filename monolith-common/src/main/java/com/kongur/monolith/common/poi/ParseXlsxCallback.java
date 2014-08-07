package com.kongur.monolith.common.poi;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;

import com.kongur.monolith.common.result.Result;

/**
 * 解析xlsx回调接口
 * 
 * @author zhengwei
 * @param <E>
 */
public interface ParseXlsxCallback<E> extends ParseCallback<E> {

    /**
     * 填充数据对象
     * 
     * @param obj
     * @param value
     * @param cellNum 从0开始
     */
    void fillObj(E obj, XSSFCell cell, String value, int cellNum, Result<List<E>> result);
}
