package com.vimdream.htool.excel.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @Title: RowUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/28 14:06
 */
public class RowUtil {

    public static Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

}
