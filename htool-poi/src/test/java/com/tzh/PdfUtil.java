package com.tzh;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class PdfUtil {
	/**
	 * 
	 * @param o 写入的数据
	 * @param out 自定义保存pdf的文件流
	 * @param templatePath pdf模板路径
	 */
	// 利用模板生成pdf
    public static void fillTemplate(Map<String,Object> o, OutputStream out, String templatePath) {
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            //addWaterMar(stamper.getOverContent(1), "唐钟豪", baseFont);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(baseFont);

            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                System.out.println(name);
                String value = o.get(name)!=null?o.get(name).toString():null;
                form.setField(name,value);
            }



            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);


            doc.open();

            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }
    }

    /**
     * 添加水印
     * @param waterMar
     * @param content
     * @throws IOException
     * @throws DocumentException
     */
    public static void addWaterMar(PdfContentByte waterMar, String content, BaseFont baseFont) throws IOException, DocumentException {
        // 添加水印
//        PdfContentByte waterMar = writer.getDirectContent();

        waterMar.beginText();
//        waterMar.setFontAndSize(baseFont, 400);

        PdfGState gs = new PdfGState();
        // 设置填充字体不透明度为0.4f
        gs.setFillOpacity(0.4f);
        // 设置水印字体参数及大小                                  (字体参数，字体编码格式，是否将字体信息嵌入到pdf中（一般不需要嵌入），字体大小)
//        waterMar.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 60);
        waterMar.setFontAndSize(baseFont, 60);
        // 设置透明度
        waterMar.setGState(gs);
        // 设置水印对齐方式 水印内容 X坐标 Y坐标 旋转角度
        waterMar.showTextAligned(Element.ALIGN_RIGHT, content , 500, 430, 45);
        // 设置水印颜色
        waterMar.setColorFill(BaseColor.BLACK);
        //结束设置
        waterMar.endText();
//        waterMar.stroke();
    }
  
}