package com.vimdream.htool.excel;

import com.vimdream.htool.excel.util.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExcelType {

    XLS(1, ExcelUtil.XLS),
    XLSX(2, ExcelUtil.XLSX)
    ;
    private int type;
    private String typeName;
}