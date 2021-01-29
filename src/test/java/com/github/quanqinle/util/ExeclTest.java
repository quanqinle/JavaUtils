package com.github.quanqinle.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.quanqinle.util.ReadExcel;

/**
 * @author quanql
 *
 */
public class ExeclTest {

    public static void main(String[] args) throws IOException {
        String textColor = "rgba(255, 0, 0, 1)";
        System.out.println(textColor);

        Pattern c = Pattern.compile("rgba *\\(*([0-9]+), *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(textColor);
        m.matches();

        Color awtColor = new Color(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3)), Integer.valueOf(m.group(4)));

        File file = new File("D:\\demo.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sh = wb.createSheet("Scorecard");
        XSSFCell cell = sh.createRow(0).createCell(0);
        cell.setCellValue("red text");
        XSSFFont xssfFont = wb.createFont();
        XSSFColor xssfColor = new XSSFColor(awtColor);
        xssfFont.setColor(xssfColor);
        XSSFCellStyle style = (XSSFCellStyle)wb.createCellStyle();
        style.setFont(xssfFont);
        cell.setCellStyle(style);
        
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        wb.close();
        System.out.println("end");
    }
    public Workbook createWorkbook(String filenameInResource) {
        InputStream inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("" + filenameInResource);
        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(inp);
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private InputStream getInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    // @Test
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
                rownum++;
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Test
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
            rownum++;
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
