package com.tzh;

import com.vimdream.htool.excel.listener.ColumnCellListener;
import com.vimdream.htool.excel.util.CellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.SimpleDateFormat;

/**
 * @Title: DefaultDateFormatHandler
 * @Author vimdream
 * @ProjectName htool-poi
 * @Date 2020/6/29 13:12
 */
public class DefaultDateFormatHandler implements ColumnCellListener {

    private SimpleDateFormat sdf;

    public DefaultDateFormatHandler() {
        this.sdf = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public boolean modifyCell(Workbook workbook, Cell cell, Object val, boolean isHead) {
        if (!isHead) {
            String date = this.sdf.format(val);
            cell.setCellValue(date);
            return true;
        }
        return false;
    }
}
