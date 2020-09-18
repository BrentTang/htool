package com.tzh;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * @Title: PdfTemplate
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/7/27 14:38
 */
public class PdfTemplate {

    @Test
    public void test() throws FileNotFoundException {

        HashMap<String, Object> data = new HashMap<>();
        data.put("qt", "唐");
        data.put("z", "钟");
        data.put("h", "豪");

        FileOutputStream out = new FileOutputStream("E:\\Users\\Brent\\Desktop\\123temp申请购房支持审核认定表.pdf");
        // "E:\Users\Brent\Desktop\temp申请购房支持审核认定表.pdf"
        PdfUtil.fillTemplate(data, out, "E:\\Users\\Brent\\Desktop\\temp申请购房支持审核认定表.pdf");
    }

}
