package com.tzh;

import com.alibaba.fastjson.util.IOUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class PdfWaterMarkUtils {
    public static void main(String[] args) throws DocumentException, IOException {
        FileOutputStream os = new FileOutputStream("C:/Users/12614/Desktop/1.pdf");
        FileInputStream is = new FileInputStream("C:/Users/12614/Desktop/bbb.pdf");
//        markTxt(is, os, "======", "2013");
    }

    /*public static void markTxt(String input, String out, String mainMark, String rootMark)
            throws DocumentException, IOException {
        markTxt(new File(input), FileUtils.newFile(out), mainMark, rootMark);
    }*/

    /*public static void markTxt(File input, File out, String mainMark, String rootMark)
            throws DocumentException, IOException {
        OutputStream os = new FileOutputStream(out);
        try {
            markTxt(input, os, mainMark, rootMark);
        } finally {
            IOUtils.close(os);
        }
    }*/

    /*public static void markTxt(String input, OutputStream os, String mainMark, String rootMark)
            throws DocumentException, IOException {
        markTxt(new File(input), os, mainMark, rootMark);
    }*/

    /*public static void markTxt(File input, OutputStream os, String mainMark, String rootMark)
            throws DocumentException, IOException {
        markTxt(new FileInputStream(input), os, mainMark, rootMark);
    }*/

    /*public static void markTxt(InputStream is, OutputStream os, String mainMark, String rootMark)
            throws DocumentException, IOException {
        markTxt(0.5f, 60, true, is, os, mainMark, rootMark);
    }*/

    /**
     * 
     * @param alpha        透明度 0-1
     * @param degree    角度
     * @param isUnder    水印置于文本上/下
     * @param is        输入IO
     * @param os        输出IO
     * @param mainMark    主文本
     * @param rootMark    页脚文本
     */
    public static void markTxt(float alpha, int degree, boolean isUnder, InputStream is, OutputStream os,
            String mainMark, String rootMark) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(is);
        PdfStamper stamper = new PdfStamper(reader, os);

        try {
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(alpha);

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            PdfContentByte content;
            int total = reader.getNumberOfPages() + 1;
            for (int i = 1; i < total; i++) {
                if (isUnder) {
                    content = stamper.getUnderContent(i);
                } else {
                    content = stamper.getOverContent(i);
                }
                content.setGState(gs);
                content.beginText();
                content.setColorFill(BaseColor.LIGHT_GRAY);
                content.setFontAndSize(base, 50);
                content.setTextMatrix(70, 200);
                content.showTextAligned(Element.ALIGN_CENTER, mainMark, 300, 350, degree);

                content.setColorFill(BaseColor.BLACK);
                content.setFontAndSize(base, 8);
                content.showTextAligned(Element.ALIGN_CENTER, rootMark, 300, 10, 0);
                content.endText();

            }
        } finally {
            stamper.close();
            reader.close();
            is.close();
        }
    }

    /*public static void markImage(String iconPath, InputStream is, OutputStream os, String rootMark)
            throws DocumentException, IOException {
        markImage(iconPath, 0.5f, 60, true, is, os, rootMark);
    }*/

    /**
     * 
     * @param iconPath     图标
     * @param alpha        透明度
     * @param degree    角度
     * @param isUnder    在内容下/上方加水印
     * @param is        输入IO
     * @param os        输出IO
     * @param rootMark    页脚文本描述
     */
    public static void markImage(String iconPath, float alpha, int degree, boolean isUnder, InputStream is,
            OutputStream os, String rootMark) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(is);
        PdfStamper stamper = new PdfStamper(reader, os);
        try {
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            PdfGState gs = new PdfGState();
            gs.setFillOpacity(alpha);

            PdfContentByte content;
            int total = reader.getNumberOfPages() + 1;
            for (int i = 1; i < total; i++) {
                if (isUnder) {
                    content = stamper.getUnderContent(i);
                } else {
                    content = stamper.getOverContent(i);
                }

                content.setGState(gs);
                content.beginText();

                Image image = Image.getInstance(iconPath);
                image.setAlignment(Image.LEFT | Image.TEXTWRAP);
                image.setRotationDegrees(degree);
                image.setAbsolutePosition(200, 200);
                image.scaleToFit(200, 200);

                content.addImage(image);
                content.setColorFill(BaseColor.BLACK);
                content.setFontAndSize(base, 8);
                content.showTextAligned(Element.ALIGN_CENTER, rootMark, 300, 10, 0);
                content.endText();
            }
        } finally {
            stamper.close();
            reader.close();
            is.close();
        }
    }
}