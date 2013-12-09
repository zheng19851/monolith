package com.kongur.monolith.common.poi;

import org.apache.poi.hssf.usermodel.HSSFRow;


/**
 * 导出数据回调接口
 * 
 * @author zhengwei
 * @param <T>
 */
public interface ExportCallback<T> {

    /**
     * 填充一行数据
     * 
     * @param row
     * @param t
     */
    void fillRowData(HSSFRow row, T t);
}
