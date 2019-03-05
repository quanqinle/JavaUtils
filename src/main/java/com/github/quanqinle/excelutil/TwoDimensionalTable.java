/**
 * 
 */
package com.github.quanqinle.excelutil;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Two Dimensional Table in Sheet
 * 
 * @author quanql
 *
 */
public class TwoDimensionalTable {

	/**
	 * 
	 */
	public TwoDimensionalTable() {
		HeaderRow headerRow = new HeaderRow(3, 0, 8, 0, 30);
		HeaderColumn headerColumn = new HeaderColumn(1, 0, 35, 0, 10);

		Workbook workbookTemplate = createWorkbook("structure/所有者权益变动表.xlsx");
		Workbook workbookData = createWorkbook("财务数据.xls");

		try {
			headerRow.setColsByTemplate(workbookTemplate);
			headerRow.setColsIndex(workbookData.getSheet("所有者权益变动表"));
			headerColumn.setRowsNameByTemplate(workbookTemplate);
			headerColumn.setRowsIndex(workbookData.getSheet("所有者权益变动表"));

			for (HeaderCell colCell : headerRow.getCols()) {
				System.out.printf("[%d]-->%s\n", colCell.getIndex(), colCell.getName());
			}
			for (HeaderCell colCell : headerColumn.getRows()) {
				System.out.printf("[%d]-->%s\n", colCell.getIndex(), colCell.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeWorkbook(workbookTemplate);
			closeWorkbook(workbookData);
		}
	}

	public Workbook createWorkbook(String filenameInResource) {
		Workbook workbook = null;

		try {
			InputStream inp = Thread.currentThread().getContextClassLoader().getResourceAsStream(filenameInResource);
			workbook = WorkbookFactory.create(inp);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	public void closeWorkbook(Workbook workbook) {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * main for debug
	 */
	public static void main(String[] args) {
		TwoDimensionalTable TDTable = new TwoDimensionalTable();
	}

}
