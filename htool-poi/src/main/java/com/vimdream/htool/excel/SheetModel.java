package com.vimdream.htool.excel;

import com.vimdream.htool.excel.listener.ColumnCellListener;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: SheetModel
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/29 13:44
 */
@Data
public class SheetModel {

    private AtomicInteger curRow;
    private Map<String, String> headerAlias;
    private Map<String, ColumnCellListener> cellListeners;

    public SheetModel() {
        this.curRow = new AtomicInteger();
    }
}
