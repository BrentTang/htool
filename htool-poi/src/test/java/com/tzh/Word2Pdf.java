package com.tzh;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.xml.internal.ws.util.StreamUtils;
import com.vimdream.htool.io.IOUtil;
import com.vimdream.htool.string.StringUtil;
import fr.opensagres.poi.xwpf.converter.core.BasicURIResolver;
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @Title: Word2Pdf
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/7/26 9:10
 */
public class Word2Pdf {

    public String doc2Html(String docPath, String imgPath, String ENCODING) throws Exception {
        /*String docPath = "";
        String imgPath = "";
        String ENCODING = "";*/

        File imgDir = new File(imgPath);
        String content = "";

        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(docPath));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        if (StringUtil.isNotBlank(imgPath)) {
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                    File file = new File(imgPath, suggestedName);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return imgPath + suggestedName;
                }
            });
        }
        wordToHtmlConverter.processDocumentPart(wordDocument, new Range(0, wordDocument.characterLength(), wordDocument));
        Document htmlDocument = wordToHtmlConverter.getDocument();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.transform(domSource, streamResult);

        out.close();
        content = out.toString();

        return content;
    }

    public String docx2Html(String docPath, String imgPath) throws Exception {

        /*String path = "";
        String imgPath = "";*/

        File imgDir = new File(imgPath);
        String content = "";

        FileInputStream in = new FileInputStream(docPath);
        XWPFDocument document = new XWPFDocument(in);
        XHTMLOptions options = XHTMLOptions.create();

        if (StringUtil.isNotBlank(imgPath)) {
            options.setExtractor(new FileImageExtractor(imgDir));
            options.URIResolver(new BasicURIResolver(imgPath));
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, out, options);
        out.close();
        content = out.toString();

        return content;
    }

    public String jsoup(String content) {

        org.jsoup.nodes.Document docment = Jsoup.parse(content);
        String html = docment.html();

//        IOUtil.store(html, "", false);
        return html;
    }

    /*public void html2Pdf(String html, String outPath) throws Exception {

//        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));

        ITextRenderer iTextRenderer = new ITextRenderer();
//        iTextRenderer.setDocumentFromString(html);
        iTextRenderer.setDocument(new File("E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.html"));
        //解决中文编码
        ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
        if ("linux".equals(getCurrentOperationSystem())) {
            fontResolver.addFont("/usr/share/fonts/chiness/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } else {
            fontResolver.addFont("c:/Windows/Fonts/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }

        iTextRenderer.layout();
        iTextRenderer.createPDF(new FileOutputStream(outPath));
//        document.open();
//        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()), Charset.forName("UTF-8"));
//        document.close();
    }*/

    /*public void html2Pdf3(String html, String outPath) throws Exception {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));

        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer
                , document, new ByteArrayInputStream(html.getBytes())
                , Charset.forName("UTF-8"), new AsianFontProvider());
        document.close();
    }
    class AsianFontProvider extends XMLWorkerFontProvider {
        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {
            try {
                BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                return new Font(bfChinese, size, style);
            } catch (Exception e) {
            }
            return super.getFont(fontname, encoding, size, style);
        }
    }*/

    private static String getCurrentOperationSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return os;
    }

    /*public void html2Pdf(InputStream htmlInput, String outPath) throws Exception {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
        document.open();

        XMLWorkerHelper.getInstance().parseXHtml(writer, document, htmlInput, Charset.forName("UTF-8"));
        document.close();
    }*/

    /*public void html2Pdf2(String html, String outPath) throws Exception {

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        BaseFont bfChinese;
        bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
        MyFontProvider myFontProvider = new MyFontProvider(BaseColor.BLACK, "", "", false, false, 16, 1, bfChinese);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
        document.open();

        final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
        tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
        tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);

        final CssFilesImpl cssFiles = new CssFilesImpl();
        cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
        final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        final HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(myFontProvider));
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
        final HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, writer));
        final Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

        final XMLWorker worker = new XMLWorker(pipeline, true);
        final Charset charset = Charset.forName("UTF-8");
        final XMLParser xmlParser = new XMLParser(true, worker, charset);

        ByteArrayInputStream bais = new ByteArrayInputStream(html.getBytes("UTF-8"));

        xmlParser.parse(bais, charset);

//        XMLWorkerHelper.getInstance().parseXHtml(writer, document, bais, Charset.forName("UTF-8"));
        document.close();
    }*/


    @Test
    public void test() throws Exception {
        String html = docx2Html("E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.docx", "");
//        String html = null;

//        html = jsoup(html);

//        html2Pdf(html, "E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.pdf");
//        html2Pdf3(html, "E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.pdf");
//        IOUtil.store(html, "E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.html", false);
//        html2Pdf(new FileInputStream("E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.html")
//                , "E:\\Users\\Brent\\Desktop\\金牛\\test\\张三.pdf");
    }

    /*@Test
    public void ttttes() {
        com.spire.doc.Document document = new com.spire.doc.Document();
        // "E:\Users\Brent\Desktop\金牛\test\吴二.docx"
        // "E:\Users\Brent\Desktop\毕业设计验收材料(6.29).docx"
        document.loadFromFile("E:\\Users\\Brent\\Desktop\\毕业设计验收材料(6.29).docx");

        document.saveToFile("E:\\Users\\Brent\\Desktop\\er.pdf", FileFormat.PDF);
    }*/

    @Test
    public void testss() {

        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        ArrayList<Integer> temp = new ArrayList<>();
//        list.parallelStream().forEach(i -> temp.add(i));
        list.stream().forEach(i -> temp.add(i));

        for (int i = 0; i < temp.size(); i++) {
            if (i % 15 == 0)
                System.out.println();
            System.out.print(i + "\t");
        }
        System.out.println();
        System.out.println(temp.size());
    }
}
