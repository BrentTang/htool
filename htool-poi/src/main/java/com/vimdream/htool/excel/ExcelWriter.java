package com.vimdream.htool.excel;

import com.vimdream.htool.collection.CollectionUtil;
import com.vimdream.htool.excel.listener.ColumnCellListener;
import com.vimdream.htool.excel.util.CellUtil;
import com.vimdream.htool.excel.util.RowUtil;
import com.vimdream.htool.io.IOUtil;
import com.vimdream.htool.lang.Assert;
import com.vimdream.htool.reflect.ReflectSupport;
import com.vimdream.htool.string.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: ExcelWriter
 * @Author vimdream
 * @ProjectName htool-poi
 * @Date 2020/6/28 10:36
 */
public class ExcelWriter extends ExcelBase{

    public ExcelWriter() {}

    public ExcelWriter(boolean isXlsx) {
        super(isXlsx);
    }

    public ExcelWriter(Workbook workbook, ExcelType excelType) {
        super(workbook, excelType);
    }

    public ExcelWriter merge(int lastColumn, String content) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        int rowIndex = this.getCurRow().get();
        this.merge(rowIndex, rowIndex, 0, lastColumn, content, null);
        return this;
    }

    public ExcelWriter merge(int startRow, int endRow, int startColumn, int endColumn, String content, CellStyle cellStyle) {
        CellUtil.mergingCells(this.getSheet(), startRow, endRow, 0, endColumn);

        if (StringUtil.isNotEmpty(content)) {
            Cell cell = this.getOrCreateCell(startRow, startColumn);
            CellUtil.setCellValue(cell, content, cellStyle);
        }
        this.passRows(endColumn - startColumn);
        return this;
    }

    public ExcelWriter write(Iterable<?> data, boolean isWriteKeyAsHead) {
        if (data != null) {
            boolean isFirst = true;
            Iterator<?> iterator = data.iterator();
            while (iterator.hasNext()) {
                this.weiteRow(iterator.next(), isFirst && isWriteKeyAsHead);
                if (isFirst) isFirst = false;
//                this.getCurRow().incrementAndGet();
            }
        }
        return this;
    }

    public ExcelWriter weiteRow(Object rowData, boolean isWriteKeyAsHead) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        if (rowData == null) {
            this.passCurrentRow();
            return this;
        }
        if (rowData instanceof List) {
            return writeRow((List) rowData);
        } else if (rowData instanceof Map) {
            Map rowMap = this.convert((Map) rowData);
            return this.writeRow(rowMap, isWriteKeyAsHead);
        } else {
            Map rowMap = this.convert(rowData);
            return this.writeRow(rowMap, isWriteKeyAsHead);
        }
    }

    public ExcelWriter writeRow(List rowData) {
        Iterator iterator = rowData.iterator();
        int row = this.getCurRow().get();
        int col = 0;
        while (iterator.hasNext()) {
//            CellUtil.setCellValue(getOrCreateCell(row, col), iterator.next(), CellUtil.getDefaultStyle(this.workbook));
            CellUtil.setCellValue(getOrCreateCell(row, col), iterator.next(), null);
            col++;
        }
        this.getCurRow().incrementAndGet();
        return this;
    }

    public ExcelWriter writeRow(Map rowMap, boolean isWriteKeyAsHead) {
        Iterator iterator = rowMap.keySet().iterator();
        int row = this.getCurRow().get();
//        CellStyle defaultStyle = CellUtil.getDefaultStyle(this.workbook);
        CellStyle defaultStyle = null;
        if (isWriteKeyAsHead) {
            int headCol = 0;
            for (Object key : rowMap.keySet()) {
                ColumnCellListener columnCellListener = getColumnCellListener(key.toString());
                Cell cell = getOrCreateCell(row, headCol++);
                if (columnCellListener == null || !columnCellListener.modifyCell(this.getWorkbook(), cell, key, true)) {
                    cell.setCellStyle(defaultStyle);
                    CellUtil.setCellValue(cell, key, defaultStyle);
                }
            }
            row++;
            this.getCurRow().incrementAndGet();
        }
        int col = 0;
        while (iterator.hasNext()) {
            Object key = iterator.next();
            ColumnCellListener columnCellListener = getColumnCellListener(key.toString());
            Cell cell = getOrCreateCell(row, col++);
            if (columnCellListener == null || !columnCellListener.modifyCell(this.getWorkbook(), cell, rowMap.get(key), false)) {
                cell.setCellStyle(defaultStyle);
                CellUtil.setCellValue(cell, rowMap.get(key), defaultStyle);
            }
        }
        this.getCurRow().incrementAndGet();
        return this;
    }

    /**
     * 应用别名
     * @param rowData
     * @return
     */
    private Map convert(Map rowData) {
        Map<Object, Object> rowMap = new LinkedHashMap<>();
        if (this.getHeaderAlias() == null) {
            Iterator iterator = rowData.keySet().iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                this.addHeaderAlias(key.toString(), key.toString());
            }
        }
        Iterator<String> iterator = this.getHeaderAlias().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            rowMap.put(this.getHeaderAlias().get(key), rowData.get(key));
            rowData.remove(key);
        }
        if (!CollectionUtil.isEmpty(rowData)) {
            rowMap.putAll(rowData);
        }
        return rowMap;
    }

    /**
     * 对象转Map
     * @param obj
     * @return
     */
    private Map convert(Object obj) {
        ReflectSupport reflectSupport = new ReflectSupport(obj);
        if (this.getHeaderAlias() == null) {
            this.initHeaderAliasAndColumnCellListener(obj, reflectSupport);
        }
        Iterator<Map.Entry<String, String>> iterator = this.getHeaderAlias().entrySet().iterator();
        LinkedHashMap<String, Object> rowMap = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, String> alias = iterator.next();
            rowMap.put(alias.getValue(), reflectSupport.getFieldValueForce(alias.getKey()));
        }
        return rowMap;
    }

    /**
     * 初始化别名与Cell监听器
     * @param obj
     * @param reflectSupport
     */
    public void initHeaderAliasAndColumnCellListener(Object obj, ReflectSupport reflectSupport) {
        TreeMap<Integer, String> headerAliasSort = new TreeMap<>();
        Map<String, ExcelProperty> fieldAnno = reflectSupport.getAllFieldAnno(ExcelProperty.class);
        // 有注解
        if (!CollectionUtil.isEmpty(fieldAnno)) {
            Iterator<Map.Entry<String, ExcelProperty>> iterator = fieldAnno.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ExcelProperty> field = iterator.next();
                headerAliasSort.put(field.getValue().index(), field.getKey());
            }
            Iterator<String> aliasIterator = headerAliasSort.values().iterator();
            while (aliasIterator.hasNext()) {
                String fieldName = aliasIterator.next();
                ExcelProperty excelProperty = fieldAnno.get(fieldName);
                if (Void.class.equals(excelProperty.columnCellListener())) {
                    this.addHeaderAlias(fieldName, excelProperty.alias());
                } else {
                    Object listenerInstance = ReflectSupport.getInstanceByNoArgs(excelProperty.columnCellListener());
                    if (!ColumnCellListener.class.isInstance(listenerInstance)) {
                        throw new IllegalArgumentException("请确保 ExcelProperty "
                                + excelProperty.columnCellListener().getName()
                                + "实现ColumnCellListener接口");
                    }
                    this.addHeaderAliasAndCellListener(fieldName,
                            excelProperty.alias(),
                            (ColumnCellListener) listenerInstance);
                }
            }
        } else {
            // 没有注解使用所有字段
            List<String> fieldsName = reflectSupport.getFieldsName();
            Iterator<String> iterator = fieldsName.iterator();
            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                this.addHeaderAlias(fieldName, fieldName);
            }
        }
    }

    public void flush(OutputStream outputStream) {
        try {
            this.getWorkbook().write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
