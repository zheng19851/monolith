package com.runssnail.monolith.common.poi;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.runssnail.monolith.common.result.Result;

/**
 * excel工具类
 * 
 * @author zhengwei
 */
public class ExcelParseTool {

    private static final Logger log = Logger.getLogger(ExcelParseTool.class);

    /**
     * 解析xls
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static <E> Result<List<E>> parseXls(InputStream is, ParseXlsCallback<E> callback) throws IOException {

        Result<List<E>> result = new Result<List<E>>();
        result.setSuccess(true);

        List<E> list = new ArrayList<E>();

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }

            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }

                E e = callback.createObj();

                // 循环列Cell
                for (int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++) {
                    HSSFCell hssfCell = hssfRow.getCell(cellNum);
                    if (hssfCell == null) {
                        continue;
                    }

                    String value = getValue(hssfCell);

                    callback.fillObj(e, hssfCell, value, cellNum, result);

                }

                if (callback.isValid(e)) {
                    list.add(e);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("parse excel successfully, data=" + list);
        }

        result.setResult(list);
        return result;
    }

    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            // DecimalFormat df = new DecimalFormat("0");
            // String s = df.format(hssfCell.getNumericCellValue());
            // return s;
            double v = hssfCell.getNumericCellValue();
            return String.valueOf(v);
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    /**
     * 解析Xlsx
     * 
     * @param is
     * @param callback
     * @return
     * @throws IOException
     */
    public static <E> Result<List<E>> parseXlsx(InputStream is, ParseXlsxCallback<E> callback) throws IOException {

        Result<List<E>> result = new Result<List<E>>();

        result.setSuccess(true);

        List<E> list = new ArrayList<E>();

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {

            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            // 循环行Row，如果有4条记录，xssfSheet.getLastRowNum()=3
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (log.isDebugEnabled()) {
                    log.debug("exe row, rowNum=" + rowNum + ", xssfRow=" + xssfRow);
                }

                if (xssfRow == null) {
                    continue;
                }

                E obj = callback.createObj();

                // 循环列Cell
                for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                    XSSFCell xssfCell = xssfRow.getCell(cellNum);
                    if (xssfCell == null) {
                        continue;
                    }

                    String value = getValue(xssfCell);
                    callback.fillObj(obj, xssfCell, value, cellNum, result);
                    if (!result.isSuccess()) {
                        return result;
                    }
                }

                if (callback.isValid(obj)) {
                    list.add(obj);
                }

            }
        }

        if (log.isDebugEnabled()) {
            log.debug("parse excel successfully, data=" + list);
        }

        result.setResult(list);

        return result;
    }

    private static String getValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            double v = xssfCell.getNumericCellValue();

            // DecimalFormat df = new DecimalFormat("0");
            // String s = df.format(v);
            // return s;
            return String.valueOf(v);
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("0");
        String s = df.format(2.2222211E7);
        System.out.println(s);
    }
}
