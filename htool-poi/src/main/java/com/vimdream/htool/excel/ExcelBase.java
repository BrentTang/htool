package com.vimdream.htool.excel;

import com.vimdream.htool.excel.listener.ColumnCellListener;
import com.vimdream.htool.excel.util.CellUtil;
import com.vimdream.htool.excel.util.RowUtil;
import com.vimdream.htool.io.IOUtil;
import com.vimdream.htool.lang.Assert;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title: ExcelBase
 * @Author vimdream
 * @ProjectName htool-poi
 * @Date 2020/6/29 16:32
 */
public abstract class ExcelBase {

    private Workbook workbook;
    private Sheet sheet;
    private ExcelType excelType;
    protected boolean isClosed = false;
    private SheetModel sheetModel;
    private Map<String, SheetModel> sheetModelMapping;

    public ExcelBase() {}

    public ExcelBase(boolean isXlsx) {
        if (isXlsx) {
            this.workbook = new XSSFWorkbook();
            this.excelType = ExcelType.XLSX;
        } else {
            this.workbook = new HSSFWorkbook();
            this.excelType = ExcelType.XLS;
        }
        this.sheet = getOrCreateSheet();
    }

    public ExcelBase(Workbook workbook, ExcelType excelType) {
        this.workbook = workbook;
        this.excelType = excelType;
        this.sheet = getOrCreateSheet();
    }

    protected Sheet getSheet() {
        return this.sheet;
    }

    protected Workbook getWorkbook() {
        return this.workbook;
    }

    protected AtomicInteger getCurRow() {
        return this.sheetModel.getCurRow();
    }

    protected Map<String, String> getHeaderAlias() {
        return this.sheetModel.getHeaderAlias();
    }

    protected Map<String, ColumnCellListener> getCellListeners() {
        return this.sheetModel.getCellListeners();
    }

    protected void addSheetModel(String sheetName, SheetModel sheetModel) {
        this.getSheetModelMapping().put(sheetName, sheetModel);
    }

    protected void initSheetModel(String sheetName) {
        SheetModel sheetModel = new SheetModel();
        this.addSheetModel(sheetName, sheetModel);
        this.sheetModel = sheetModel;
    }

    protected boolean switchSheetModel(String sheetName) {
        this.sheetModel = this.getSheetModelMapping().get(sheetName);
        if (this.sheetModel != null)
            return true;
        return false;
    }

    protected Map<String, SheetModel> getSheetModelMapping() {
        if (this.sheetModelMapping == null)
            this.sheetModelMapping = new HashMap<>();
        return this.sheetModelMapping;
    }

    public void addHeaderAlias(String property, String alias) {
        if (this.getHeaderAlias() == null)
            this.sheetModel.setHeaderAlias(new LinkedHashMap<>());
        this.getHeaderAlias().put(property, alias);
    }

    public void addHeaderAliasAndCellListener(String property, String alias, ColumnCellListener columnCellListener) {
        addHeaderAlias(property, alias);
        bindColumnCellListener(alias, columnCellListener);
    }

    public void bindColumnCellListener(String alias, ColumnCellListener columnCellListener) {
        if (this.getCellListeners() == null)
            this.sheetModel.setCellListeners(new HashMap<>());
        this.getCellListeners().put(alias, columnCellListener);
    }

    public ColumnCellListener getColumnCellListener(String alias) {
        if (this.getCellListeners() != null)
            return this.getCellListeners().get(alias);
        return null;
    }

    /**
     * 切换至{curSheet}索引的sheet
     * @param curSheet
     * @return
     */
    public void switchSheet(int curSheet) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        this.sheet = workbook.getSheetAt(curSheet);
        this.switchSheetModel(this.sheet.getSheetName());
    }

    /**
     * 切换至名称为{sheetName}的sheet
     * @param sheetName
     * @return
     */
    public void switchSheet(String sheetName) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        this.sheet = workbook.getSheet(sheetName);
        if (this.sheet == null)
            throw new IllegalArgumentException("无效的sheet name");
        if (!this.switchSheetModel(this.sheet.getSheetName()))
            this.initSheetModel(this.sheet.getSheetName());
    }

    /**
     * 切换sheet, 若sheet不存在则创建
     * @param sheetName
     * @return
     */
    public void switchSheetOrCreate(String sheetName) {
        try {
            this.switchSheet(sheetName);
        } catch (IllegalArgumentException e) {
            this.createSheet(sheetName);
        }
    }

    public void createSheet(String sheetName) {
        this.sheet = this.workbook.createSheet(sheetName);
        if (!this.switchSheetModel(this.sheet.getSheetName()))
            this.initSheetModel(this.sheet.getSheetName());
    }

    protected Sheet getOrCreateSheet() {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        int numberOfSheets = this.workbook.getNumberOfSheets();
        Sheet sheet = null;
        if (numberOfSheets > 0) {
            sheet = this.workbook.getSheetAt(0);
        } else {
            sheet = workbook.createSheet();
        }
        if (!this.switchSheetModel(sheet.getSheetName())) {
            this.initSheetModel(sheet.getSheetName());
        }
        return sheet;
    }

    /**
     * 设置当前行
     * @param curRow
     * @return
     */
    public void switchRow(int curRow) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        this.getCurRow().set(curRow);
    }

    /**
     * 跳过当前行
     * @return
     */
    public void passCurrentRow() {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        this.getCurRow().incrementAndGet();
    }

    /**
     * 跳过{rowCount}行
     * @param rowCount
     * @return
     */
    public void passRows(int rowCount) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        this.getCurRow().addAndGet(rowCount);
    }

    public Cell getOrCreateCell(int rowIndex, int colIndex) {
        Assert.isFalse(this.isClosed, "ExcelWriter 已经被关闭", new Object[0]);
        return this.getCell(rowIndex, colIndex, true);
    }

    public Cell getCell(int rowIndex, int colIndex, boolean isCreateIfNotExist) {
        Row row = isCreateIfNotExist ? RowUtil.getOrCreateRow(this.sheet, rowIndex) : this.sheet.getRow(rowIndex);
        if (null != row) {
            return isCreateIfNotExist ? CellUtil.getOrCreateCell(row, colIndex) : row.getCell(colIndex);
        } else {
            return null;
        }
    }

    public void flush(OutputStream outputStream) {
        try {
            this.workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        IOUtil.close(this.workbook);
        this.sheet = null;
        this.excelType = null;
        this.isClosed = true;
        this.sheetModel = null;
        this.sheetModelMapping = null;
    }
}
