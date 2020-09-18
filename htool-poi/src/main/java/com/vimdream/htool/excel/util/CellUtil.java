package com.vimdream.htool.excel.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import java.util.Calendar;
import java.util.Date;

/**
 * @Title: CellUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/28 13:58
 */
public class CellUtil {

    public static final short DEFAULT_FONT_SIZE = 16;

    public static Cell getOrCreateCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (null == cell) {
            cell = row.createCell(cellIndex);
        }

        return cell;
    }

    public static int mergingCells(Sheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn);
        return sheet.addMergedRegion(cellRangeAddress);
    }

    /*public static void setCellValue(Cell modifyCell, Object value, CellStyle cellStyle, boolean isHeader) {
        if (null != modifyCell) {
            if (null != cellStyle) {
                modifyCell.setCellStyle(cellStyle);
            }

            setCellValue(modifyCell, value, (CellStyle)null);
        }
    }*/

    public static void setCellValue(Sheet sheet, int row, int col, Object value, CellStyle style) {
        setCellValue(getOrCreateCell(RowUtil.getOrCreateRow(sheet, row), col), value, style);
    }

    public static void setCellValue(Cell cell, Object value, CellStyle style) {
        if (null != cell) {
            if (null != style) {
                cell.setCellStyle(style);
            }

            if (null == value) {
                cell.setCellValue("");
            } else if (value instanceof Date) {
                cell.setCellValue((Date)value);
            }  else if (value instanceof Calendar) {
                cell.setCellValue((Calendar)value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean)value);
            } else if (value instanceof RichTextString) {
                cell.setCellValue((RichTextString)value);
            } else if (value instanceof Number) {
                cell.setCellValue(((Number)value).doubleValue());
            } else {
                cell.setCellValue(value.toString());
            }

        }
    }

    public static CellStyle getDefaultStyle(Workbook workbook) {
        return getCellStyle(workbook, null, DEFAULT_FONT_SIZE, null);
    }

    public static CellStyle getCellStyle(Workbook workbook, IndexedColors fgc, short fontSize, IndexedColors fontColor) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (fgc != null) {
            cellStyle.setFillForegroundColor(fgc.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        Font font = getFontStyle(workbook, fontSize, fontColor);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static Font getFontStyle(Workbook workbook, short size, IndexedColors color) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(size);
        if (color != null) {
            font.setColor(color.getIndex());
        }
        return font;
    }
}
