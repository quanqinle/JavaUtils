/**
 * @project com.github.quanqinle.util
 *
 * @author  权芹乐
 * @created 2020-09-21
 */
package com.github.quanqinle.util;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author 权芹乐
 *
 */
public class ReadExcelFile {
    XSSFWorkbook wb;
    XSSFSheet sheet;

    public ReadExcelFile(String excelPath) {
        try {
            File src = new File(excelPath);
            FileInputStream fis = new FileInputStream(src);
            wb = new XSSFWorkbook(fis);
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getData(int sheetnumber, int row, int column) {
        sheet = wb.getSheetAt(sheetnumber);
        String data = sheet.getRow(row).getCell(column).getStringCellValue();
        return data;
    }

    public int getRowCount(int sheetIndex) {
        int row = wb.getSheetAt(sheetIndex).getLastRowNum();
        row = row + 1;
        return row;
    }

    /**
     * @author 权芹乐
     * @param args
     */
    public static void main(String[] args) {
        ReadExcelFile config = new ReadExcelFile("C:\\Users\\quanql\\Desktop\\回归测试功能点.xlsx");
        int rows = config.getRowCount(0);
        System.out.println("rows = " + rows);
        Object[][] credentials = new Object[rows][2];
        for (int i = 0; i < rows; i++) {
//            System.out.println("i = " + i);
            credentials[i][0] = config.getData(0, i, 0);
            System.out.printf("data(%d) = %s\n", i, credentials[i][0]);
        }
    }

}
