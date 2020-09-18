package com.tzh;

import com.deepoove.poi.el.Name;
import com.vimdream.htool.excel.ExcelProperty;

import java.util.Date;

/**
 * @Title: User
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/29 11:38
 */
public class User {

    @ExcelProperty(alias = "姓名", index = 2)
    @Name("name1")
    private String name;
    @ExcelProperty(alias = "年龄", index = 0)
    @Name("age1")
    private Integer age;
    @ExcelProperty(alias = "日期", index = 99, columnCellListener = DefaultDateFormatHandler.class)
    private Date date;

    public User(String name, Integer age, Date date) {
        this.name = name;
        this.age = age;
        this.date = date;
    }
}
