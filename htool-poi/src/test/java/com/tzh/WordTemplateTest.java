package com.tzh;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.RowRenderData;
import com.tzh.word.DetailTablePolicy;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Title: WordTemplateTest
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/7/8 16:34
 */
public class WordTemplateTest {

    @Test
    public void test() throws Exception {
        Account account = new Account();
        account.setName("唐钟豪");
        account.setAge(20);
        account.setT1("haha");
        account.setT2("ehe");
        AccountVo accountVo = new AccountVo();
        accountVo.setGender("男");
        accountVo.setPhone("19182255027");
        account.setAccountVo(accountVo);

        HashMap<String, Object> data = new HashMap<>();
        data.put("talentInfo.name", "haha");

        /*HashMap<String, Object> renderData = new HashMap<>();
        renderData.put("")*/
        ConfigureBuilder builder = Configure.newBuilder();
        Configure configure = builder.build();

        // 模板文件
//        String templatePath = "E:\\Users\\Brent\\Desktop\\test.docx";
        String templatePath = "D:\\wordtemplate\\2019申请购房支持审核认定表.docx";
        InputStream is = new FileInputStream(new File(templatePath));

        // 输出位置
        String outPath = "D:\\wordtemplate\\2019申请购房支持审核认定表123.docx";
        OutputStream os = new FileOutputStream(new File(outPath));
        // 编译,导入策略插件,并渲染数据
        XWPFTemplate template = XWPFTemplate.compile(is, configure).render(data);
        // 输出
        template.write(os);
        os.flush();
    }

    @Test
    public void testt() throws IOException {

        RowRenderData row1 = RowRenderData.build("审核部门意见", "区组织部", "通过", "经相关部门联合初审，该申请人符合购房支持人才认定条件。");
        RowRenderData row2 = RowRenderData.build("", "区社科联", "通过", "");
        List<RowRenderData> rowRenderDataList = Arrays.asList(row2, row1);

        HashMap<Object, Object> data = new HashMap<>();
        data.put("verifyDept", rowRenderDataList);

        File file = new File("E:\\Users\\Brent\\Desktop\\申请购房支持审核认定表 后台导出.docx");

        Configure config = Configure.newBuilder().bind("verifyDept", new DetailTablePolicy()).build();

        XWPFTemplate render = XWPFTemplate.compile(file, config).render(data);

        render.writeToFile("E:\\Users\\Brent\\Desktop\\tt申请购房支持审核认定表 后台导出.docx");
    }

    @Test
    public void ttt() {
        String url = "http://192.168.0.104:8082/upload/JnDeclare/images/5d00c3d4eda74ee2ae93e705d35d2a29.pdf";

        String temp = url.substring(url.indexOf("/upload/"));
        File file = new File("D:" + File.separator + temp.replace("/", File.separator));
        System.out.println(file);
    }

}
