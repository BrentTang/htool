package com.vimdream.htool.excel.listener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public interface ColumnCellListener {

    boolean modifyCell(Workbook workbook, Cell cell, Object val, boolean isHead);

}
