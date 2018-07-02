package com.github.quanqinle.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;

import com.github.quanqinle.util.ReadExcel;

/**
 * @author quanql
 *
 */
public class ExeclTest {

	private InputStream getInputStream(String fileName) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
	}

//	@Test
	public void readExcel_01() {
		// Use an InputStream, needs more memory
		Workbook workbook = null;

		try {
			workbook = WorkbookFactory.create(getInputStream("2007_1.xlsx"));
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}

		for (Sheet sheet : workbook) {
			int rownum = 0;
			for (Row row : sheet) {
				if (rownum > 10) {
					break;
				}
				String line = "";
				for (Cell cell : row) {
					line = line + cell.getAddress().toString() + ":" + ReadExcel.getValue(cell) + ", ";
				}
				System.err.println(row.getRowNum() + "-->" + line);
				rownum ++;
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void readExcel_02() {
		// Use an InputStream, needs more memory
		Workbook workbook = null;

		try {
			workbook = WorkbookFactory.create(getInputStream("财务数据.xls"));
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheet("所有者权益变动表");

		int rownum = 0;
		for (Row row : sheet) {
			if (rownum > 10) {
				break;
			}
			String line = "";
			for (Cell cell : row) {
				line = line + ReadExcel.getValue(cell) + ", ";
			}
			System.err.println(rownum + "-->" + line);
			rownum ++;
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
