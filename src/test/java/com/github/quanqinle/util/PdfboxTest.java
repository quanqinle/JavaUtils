/**
 * 
 */
package com.github.quanqinle.util;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * note： 除了pdfbox库还有iText比较有名，我还没了解。
 * 
 * @author quanql
 *
 */
public class PdfboxTest {
	public static void main(String[] args) throws IOException {

		// Loading an existing document
		File file = new File("D:/Java8.pdf");
		PDDocument doc = PDDocument.load(file);

		// Listing the number of existing pages
		System.out.println("total page count: " + doc.getNumberOfPages());

		/**
		 * note! 请倒序删，否则破坏了预期排序
		 */
		int[] pages = { 378, 133, 77, 33 };
		for (int i : pages) {
			// removing the pages
			doc.removePage(i - 1);
		}

		// Saving the document
		doc.save("D:/Java8-new.pdf");

		// Closing the document
		doc.close();
	}

}
