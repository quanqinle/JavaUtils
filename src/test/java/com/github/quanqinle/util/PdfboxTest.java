/**
 * 
 */
package com.github.quanqinle.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 注：除了pdfbox库还有iText比较有名，据说iText更强大，但许可太严格，暂时没去了解。
 * 
 * @author 权芹乐
 *
 */
public class PdfboxTest {

	public static void main(String[] args) throws IOException {
		testGetAreaContent();
	}

	public static void handlePdf() throws IOException {
		// Loading an existing document
		File file = new File("C:\\Users\\quanql\\Desktop\\《码出高效：Java开发手册》_杨冠宝等- AiBooKs.Cc.pdf");
		PDDocument doc = PDDocument.load(file);

		// Listing the number of existing pages
		LogUtil.info("total page count: " + doc.getNumberOfPages());

		/**
		 * note! 请倒序删，否则破坏了预期排序
		 */
		int[] pages = { 378, 133, 77, 33 };
		for (int i : pages) {
			// removing the pages
			doc.removePage(i - 1);
		}

		// Saving the document
		doc.save("C:\\Users\\quanql\\Desktop\\《码出高效：Java开发手册》_杨冠宝等- AiBooKs.new.pdf");

		// Closing the document
		doc.close();
	}

	public static void testGetAreaContent() {
		String pdfPath = "D:\\218828-20200514.pdf";

		Rectangle textRrect = new Rectangle(420, 80, 130, 245);

		String strContent = PdfUtil.readTextByRectangel(pdfPath, 0, textRrect);

		LogUtil.info(strContent);

		// 保存图片
		Rectangle imgRrect = new Rectangle(0, 0, 100, 100);
		BufferedImage bufImage = PdfUtil.readImageByRectangel(pdfPath, 0, imgRrect);

		File outputfile = new File("D:\\pdfImage2.png");
		try {
			ImageIO.write(bufImage, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			LogUtil.info("图片内容：" + readQRCode("D:\\pdfImage2.png"));
		} catch (NotFoundException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static String readQRCode(String filePath) throws FileNotFoundException, IOException, NotFoundException {
		Map<DecodeHintType, ErrorCorrectionLevel> decodeMap = new HashMap<DecodeHintType, ErrorCorrectionLevel>();
		return new MultiFormatReader().decode(
		    new BinaryBitmap(
		        new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath))))),
		    decodeMap).getText();
	}
}
