package com.vimdream.htool.excel.util;

import com.vimdream.htool.excel.ExcelType;
import com.vimdream.htool.excel.ExcelWriter;
import com.vimdream.htool.string.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Title: ExcelUtil
 * @Author vimdream
 * @ProjectName htool-poi
 * @Date 2020/6/28 11:04
 */
public class ExcelUtil {

    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";

    public static ExcelWriter getWriter() {
        return new ExcelWriter(false);
    }

    public static ExcelWriter getWriter(boolean isXlsx) {
        return new ExcelWriter(isXlsx);
    }

    public static ExcelWriter getWriter(FileInputStream inputStream, ExcelType excelType) {
        try {
            switch (excelType) {
                case XLS:
                    return new ExcelWriter(new HSSFWorkbook(inputStream), excelType);
                case XLSX:
                    return new ExcelWriter(new XSSFWorkbook(inputStream), excelType);
                default:
                    return new ExcelWriter(new HSSFWorkbook(inputStream), excelType);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("excel文件解析失败");
        }
    }

    public static ExcelWriter getWriter(File templateExcel) {
        if (templateExcel != null && templateExcel.exists() && templateExcel.isFile()) {
            try {
                return getWriter(new FileInputStream(templateExcel), getExcelType(templateExcel.getName()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("excel文件解析失败");
            }
        } else
            throw new RuntimeException("未知的excel文件");
    }

    public static ExcelWriter getWriter(String templatePath) {
        if (StringUtil.isNotBlank(templatePath))
            return getWriter(new File(templatePath));
        return null;
    }

    public static ExcelType getExcelType(String name) {
        String extendName = name.substring(name.lastIndexOf(".") + 1);
        switch (extendName) {
            case XLS:
                return ExcelType.XLS;
            case XLSX:
                return ExcelType.XLSX;
            default:
                throw new RuntimeException("未知的文件类型");
        }
    }
}
