package com.github.quanqinle.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.util.Matrix;

/**
 * PDF工具
 * 
 * @author 权芹乐
 * 
 *         reference: https://github.com/apache/pdfbox/tree/trunk/examples/
 *         https://github.com/dongdongdeng/pdfboxDemo
 *         https://github.com/vinsguru/pdf-util
 *
 */
public class PdfUtil {

    private static PDFont FONT = PDType1Font.HELVETICA;
    private static float FONT_SIZE = 10;
    private static final float LEADING = -1.5f * FONT_SIZE;

    public PdfUtil() {
        // Note: when using PDFBox with Java 8 before 1.8.0_191 or Java 9 before 9.0.4
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    /**
     * Get the page count of the document.
     * 
     * @param file
     *            Absolute file path
     * @return int No of pages in the document.
     * @throws java.io.IOException
     *             when file is not found.
     */
    public int getPageCount(String file) throws IOException {
        LogUtil.info("file :" + file);
        PDDocument doc = PDDocument.load(new File(file));
        int pageCount = doc.getNumberOfPages();
        LogUtil.info("pageCount :" + pageCount);
        doc.close();
        return pageCount;
    }

    /**
     * 提取pdf中所有文本
     * 
     * @param pdfName
     * @param outFileName
     * @throws IOException
     */
    public static String extractAllText(String pdfName, String outFileName) throws IOException {

        String content = null;
        try (PDDocument pdfDoc = PDDocument.load(new File(pdfName));
                Writer writer = new OutputStreamWriter(new FileOutputStream(outFileName), "UTF-8");) {

            AccessPermission ap = pdfDoc.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("You do not have permission to extract text");
            }

            PDFTextStripper stripper = new PDFTextStripper();

            /*
             * 排序
             */
            stripper.setSortByPosition(true);

            /*
             * pdfbox对中文默认是用空格分隔每一个字，通过这个语句消除空格
             */
            // stripper.setWordSeparator("");

            /*
             * 设置转换的开始页、结束页。不配置时，默认全部
             */
            stripper.setStartPage(0);
            stripper.setEndPage(pdfDoc.getNumberOfPages());

            stripper.writeText(pdfDoc, writer);

            content = stripper.getText(pdfDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 根据指定文件页码的指定区域读取图片
     * 
     * @param filePath
     *            PDF文件路径
     * @param iPage
     *            PDF页码。base 0
     * @param imgRrect
     *            读取图片的区域
     * @return 图片内容
     */
    public static BufferedImage readImageByRectangel(String filePath, int iPage, Rectangle imgRrect) {

        BufferedImage bufImage = null;
        try (PDDocument pdfDoc = PDDocument.load(new File(filePath))) {
            AccessPermission ap = pdfDoc.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("You do not have permission to extract text");
            }

            // 获取渲染器，主要用来后面获取BufferedImage
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);

            bufImage = pdfRenderer.renderImage(iPage);
            LogUtil.info("页面宽带：" + bufImage.getWidth());
            LogUtil.info("页面高带：" + bufImage.getHeight());

            // 截取指定位置生产图片
            bufImage = bufImage.getSubimage(imgRrect.x, imgRrect.y, imgRrect.width, imgRrect.height);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bufImage;
    }

    /**
     * 根据指定文件页码的指定区域读取文字
     * 
     * @param filePath
     *            PDF文件路径
     * @param iPage
     *            PDF页码。base 0
     * @param textRrect
     *            读取文字的区域
     * @return 文字内容
     */
    public static String readTextByRectangel(String filePath, int iPage, Rectangle textRrect) {

        String REGION_NAME = "content";
        String textContent = "";

        try (PDDocument pdfDoc = PDDocument.load(new File(filePath))) {
            AccessPermission ap = pdfDoc.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("You do not have permission to extract text");
            }

            // 获取指定的PDF页
            PDPage pdfPage = pdfDoc.getPage(iPage);

            // 获取指定位置的文字（文字剥离器）
            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
            textStripper.setSortByPosition(true);
            textStripper.addRegion(REGION_NAME, textRrect);
            textStripper.extractRegions(pdfPage);

            textContent = textStripper.getTextForRegion(REGION_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return textContent;

    }

    /**
     * 把每一页保存成png图片
     * 
     * @param pdfPath
     * @param imgDir
     * @throws InvalidPasswordException
     * @throws IOException
     */
    public static void convertToImages(String pdfPath, String imgDir) throws InvalidPasswordException, IOException {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        new File(imgDir).mkdirs();

        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bufImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            // ImageIOUtil.writeImage(bufImage, imgDir + "/" + (page + 1) + ".png", 300);
            ImageUtil.saveImage(bufImage, imgDir + "/" + (page + 1) + ".png");
        }
        document.close();
    }

    /**
     * 文本的实际宽度
     * 
     * @param txt
     * @return
     * @throws Exception
     */
    private static float realWidth(String txt) throws Exception {
        float realWidth = FONT_SIZE * FONT.getStringWidth(txt) / 1000;
        return realWidth;
    }

    /**
     * 全文的添加文字水印
     * 
     * @param pdfPath
     * @param watermark
     * @throws Exception
     */
    public static void addWaterMark(String pdfPath, String watermark) throws Exception {
        PDDocument document = PDDocument.load(new File(pdfPath));
        for (PDPage everyPage : document.getPages()) {
            PDPageContentStream cs = new PDPageContentStream(document, everyPage, PDPageContentStream.AppendMode.APPEND,
                    true, true);

            float fontSize = 80.0f;
            PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
            // 透明度
            r0.setNonStrokingAlphaConstant(0.3f);
            r0.setAlphaSourceFlag(true);

            cs.setGraphicsStateParameters(r0);
            cs.setNonStrokingColor(188, 188, 188);
            cs.beginText();

            // InputStream inFont = getClassLoader().getResourceAsStream("simfang.ttf");
            PDType0Font font = PDType0Font.load(document, new File("D:\\simfang.ttf"));

            cs.setFont(font, fontSize);
            // 获取旋转实例
            cs.setTextMatrix(Matrix.getRotateInstance(45, 320f, 150f));
            cs.showText(watermark);
            cs.endText();

            cs.close();
        }

    }

    /**
     * 创建空白pdf
     * 
     * @author 权芹乐
     * @param file
     *            文件完整路径
     * @throws Exception
     */
    public static void createBlankPdf(String file) throws Exception {
        // Creating PDF document object
        PDDocument document = new PDDocument();
        // Creating a blank page
        PDPage blankPage = new PDPage();
        // Adding the blank page to the document
        document.addPage(blankPage);
        // Saving the document
        document.save(file);

        LogUtil.info("PDF created");
        // Closing the document
        document.close();
    }

    /**
     * 删除指定页码，新文件保存在相同路径下
     * 
     * @param pdfPath
     * @param pages
     *            页码。 1 based index to page number.
     * @throws IOException
     */
    public static void removePages(String pdfPath, Integer[] pages) throws IOException {
        // Loading an existing document
        PDDocument doc = PDDocument.load(new File(pdfPath));

        int pageCnt = doc.getNumberOfPages();
        // Listing the number of existing pages
        LogUtil.info("total page count: " + pageCnt);

        /**
         * 倒序删！否则破坏了预期排序
         */
        Arrays.sort(pages, Collections.reverseOrder());
        for (int i : pages) {
            int rmPage = i - 1;
            if (0 <= rmPage || rmPage <= pageCnt - 1) {
                // removing the pages
                doc.removePage(rmPage);
            }
        }

        // Saving the document
        doc.save(pdfPath.replace(".pdf", ".new.pdf"));

        // Closing the document
        doc.close();
    }

    /**
     * 添加图片 TODO
     * 
     * @param pdfPath
     * @param iPage
     *            PDF页码。base 0
     * @param imagePath
     * @throws Exception
     * @throws IOException
     */
    public static void addImage(String pdfPath, int iPage, String imagePath) throws Exception, IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));

        // Retrieving the pages of the document, and using PREPEND mode
        PDPage page = document.getPage(iPage);
        PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.PREPEND, false);

        PDType0Font font = PDType0Font.load(document, new File("D:\\simfang.ttf"));

        // Setting the font to the Content stream
        contentStream.setFont(font, 10);

        // Loading img from file
        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);

        // Draw image by x y width height
        contentStream.drawImage(pdImage, 480, 10, 100, 100); // TODO

        // Closing the content stream
        contentStream.close();

        // Saving the document
        document.save(pdfPath.replace(".pdf", ".new.pdf"));

        // Closing the document
        document.close();
    }

    /**
     * 指定位置添加文字
     * 
     * @param pdfPath
     * @param iPage
     *            PDF页码。base 0
     * @param txt
     * @param x
     * @param y
     * @throws Exception
     * @throws IOException
     */
    public static void addText(String pdfPath, int iPage, List<String> txtList, float x, float y)
            throws Exception, IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));

        // Retrieving the pages of the document, and using PREPEND mode
        PDPage page = document.getPage(iPage);
        PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.PREPEND, false);

        // Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
        // Setting the leading
        contentStream.setLeading(14.5f);
        // Setting the position for the line
        contentStream.newLineAtOffset(x, y);

        // Begin the Content stream
        contentStream.beginText();

        for (String txt : txtList) {
            // Adding text in the form of string
            contentStream.showText(txt);
            contentStream.newLine();
        }

        // Ending the content stream
        contentStream.endText();

        // Closing the content stream
        contentStream.close();

        // Saving the document
        document.save(pdfPath.replace(".pdf", ".new.pdf"));

        // Closing the document
        document.close();
    }

    /**
     * 增加一段文本
     * 
     * @param contentStream
     * @param width
     * @param sx
     * @param sy
     * @param text
     * @param justify
     * @throws IOException
     */
    private static void addParagraph(PDPageContentStream contentStream, float width, float sx, float sy, String text,
            boolean justify) throws IOException {

        contentStream.beginText();

        List<String> lines = new ArrayList<>();
        parseLinesRecursive(text, width, lines);

        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.newLineAtOffset(sx, sy);
        for (String line : lines) {
            float charSpacing = 0;
            if (justify) {
                if (line.length() > 1) {
                    float size = FONT_SIZE * FONT.getStringWidth(line) / 1000;
                    float free = width - size;
                    if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                        charSpacing = free / (line.length() - 1);
                    }
                }
            }
            contentStream.setCharacterSpacing(charSpacing);
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, LEADING);
        }

        contentStream.endText();
    }

    /**
     * 递归分析文本，并按宽度分割成N行
     * 
     * @param text
     * @param width
     * @param lines
     * @return
     * @throws IOException
     */
    private static List<String> parseLinesRecursive(String text, float width, List<String> lines) throws IOException {
        String tmpText = text;
        for (int i = 0; i < text.length(); i++) {
            tmpText = text.substring(0, text.length() - i);

            float realWidth = FONT_SIZE * FONT.getStringWidth(tmpText) / 1000;

            if (realWidth > width) {
                continue;
            } else {
                lines.add(tmpText);

                if (0 != i) {
                    parseLinesRecursive(text.substring(text.length() - i), width, lines);
                }

                break;
            }
        }

        return lines;
    }

    /**
     * 合并多个pdf
     * 
     * @param sourceList
     * @param destPdf
     * @throws Exception
     */
    public static void mergePdfs(List<String> sourceList, String destPdf) throws Exception {

        // Instantiating PDFMergerUtility class
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        // Setting the destination file
        pdfMerger.setDestinationFileName(destPdf);

        InputStream inputStream = null;
        for (String source : sourceList) {
            inputStream = new FileInputStream(source);
            pdfMerger.addSource(inputStream);
        }

        // Merging the document files
        // pdfMerger.appendDocument(destination, source);
        pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly()/* DocumentMergeMode.PDFBOX_LEGACY_MODE */);

    }

    /**
     * 提取连续页，导出先文件
     * 
     * @author 权芹乐
     * @param srcFileName
     * @param dstFileName
     * @param startPage
     *            The first page you want extracted (inclusive)
     * @param endPage
     *            The last page you want extracted (inclusive)
     */
    public static void extractPages(String srcFileName, String dstFileName, int startPage, int endPage) {
        try (PDDocument srcDoc = PDDocument.load(new File(srcFileName))) {
            PageExtractor pageExtractor = new PageExtractor(srcDoc, startPage, endPage);

            try (PDDocument dstDoc = pageExtractor.extract()) {
                dstDoc.save(dstFileName);
                LogUtil.info("extract pdf done.");
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }
    }
}
