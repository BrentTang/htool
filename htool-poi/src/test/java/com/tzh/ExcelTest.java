package com.tzh;

import com.vimdream.htool.collection.CollectionUtil;
import com.vimdream.htool.excel.ExcelWriter;
import com.vimdream.htool.excel.util.CellUtil;
import com.vimdream.htool.excel.util.ExcelUtil;
import org.junit.Test;
import sun.nio.ch.ThreadPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Title: ExcelTest
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/28 17:40
 */
public class ExcelTest {

    @Test
    public void test() throws FileNotFoundException {
        File file = new File("D:/temp/test113.xls");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ExcelWriter writer = ExcelUtil.getWriter("D:/temp/test112.xls");
        List<String> l1 = Arrays.asList("12", "123");
        List<String> l2 = Arrays.asList("12", "123");
        List<String> l3 = Arrays.asList("12", "123");
        List<String> l4 = Arrays.asList("12", "123");

        List<List<String>> lists = Arrays.asList(l1, l2, l3, l4);

        writer.passRows(10);

        writer.write(lists, true);
        writer.flush(fileOutputStream);
        writer.close();
    }

    @Test
    public void test1() throws FileNotFoundException {
        File file = new File("D:/temp/test113.xls");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        Map m1 = new HashMap<>();
        m1.put("name", "zhangsanzhangsanzhangsan");
        m1.put("age", 10);
        m1.put("address", "1919");
        m1.put("date", new Date());

        Map m2 = new HashMap<>();
        m2.put("name", "lisi");
        m2.put("age", 18);
        m2.put("address", "1919");
        m2.put("date", new Date());

        List<Map> maps = Arrays.asList(m1, m2);

        ExcelWriter writer = ExcelUtil.getWriter("D:/temp/test112.xls");

        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("age", "年龄");

        writer.bindColumnCellListener("姓名", (workbook, cell, val, isHead) -> {
            if (isHead) {
                CellUtil.setCellValue(cell, val, CellUtil.getDefaultStyle(workbook));
                return true;
            }
            if ("lisi".equals(val)) {
                CellUtil.setCellValue(cell, val.toString() + "$", CellUtil.getDefaultStyle(workbook));
                return true;
            }
            return false;
        });

        writer.passRows(10);

        writer.merge(1, "学生信息");
//        writer.autoSizeColumn(0);
        writer.write(maps, true);
        writer.flush(fileOutputStream);
        writer.close();
    }

    @Test
    public void test2() throws FileNotFoundException {
        File file = new File("D:/temp/test115.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            List l = new ArrayList();
            for (int j = 0; j < 20; j++) {
                l.add(i + j);
            }
            writer.writeRow(l);
//            writer.passCurrentRow();
            /*if (i == 60000)
                writer.flush(fileOutputStream);*/
        }

        writer.flush(fileOutputStream);
        long r = System.currentTimeMillis() - l1;
        writer.close();
        System.out.println("time:" + r);
    }

    @Test
    public void testeets() throws FileNotFoundException {
        List<User> users = Arrays.asList(
                new User("zhangsan", 18, new Date()),
                new User("李四", 18, new Date()),
                new User("王五", 18, new Date())
        );

        ExcelWriter writer = ExcelUtil.getWriter();

        writer.switchSheetOrCreate("hatta");
        writer.write(users, true);

        writer.switchSheetOrCreate("好歌");
        writer.passCurrentRow();
        writer.write(users, false);

        writer.switchSheetOrCreate("haha");
        writer.passRows(2);
        writer.write(users, true);

        File file = new File("D:/temp/test117.xls");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        writer.flush(fileOutputStream);
        writer.close();

    }

    @Test
    public void testtssss() {
        ExcelWriter writer = ExcelUtil.getWriter("D:/temp/test116.xls");

        writer.switchSheet("sheet0");

        writer.close();
        writer.switchSheet(0);

        ExecutorService pool = Executors.newCachedThreadPool();
        ThreadPoolExecutor pp = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

    }

    @Test
    public void test12() {
        Integer a, b;
        a = 1000;
//        b = 100;
        System.out.println(a == 1000);

//        System.out.println(Integer.MAX_VALUE);
    }
}
