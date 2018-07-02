/**
 * 
 */
package com.github.quanqinle.excelutil;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 列表头
 * 
 * @author quanql
 *
 */
public class HeaderColumn {
  /**
   * 表头层级数
   */
  private int multiLevel = 1;
  /**
   * 表头单元格列表（可见列，与hiddenCols相对）
   */
  private List<HeaderCell> cols = null;
  /**
   * 表头单元格列表（隐藏列）
   */
  private List<HeaderCell> hiddenCols = null;

  /**
   * @return the multiLevel
   */
  public int getMultiLevel() {
    return multiLevel;
  }

  /**
   * @param multiLevel the multiLevel to set
   */
  public void setMultiLevel(int multiLevel) {
    this.multiLevel = multiLevel;
  }

  /**
   * @return the cols
   */
  public List<HeaderCell> getCols() {
    return cols;
  }

  /**
   * @param cols the cols to set
   */
  public void setCols(List<HeaderCell> cols) {
    this.cols = cols;
  }

  /**
   * 
   */
  public HeaderColumn() {
  }
  
  /**
   * 根据excel模板初始化列表头name等信息
   * 
   * @param workbook
   */
  public void setColsByTemplate(Workbook workbook) {
    Sheet sheet = workbook.getSheet("列表头");
    int header = 2;
    for (Row row : sheet) {
      if (header > 0) { // 跳过表头
        header --;
        continue;
      }
      HeaderCell c = new HeaderCell();
      String name = ;
      String alias = ;
      String preName = ;
      c.setName(StringUtils.deleteWhitespace(row.getCell(0).getStringCellValue()));
      c.setAliasName(row.getCell(1).getStringCellValue());
      c.setPreCellName(row.getCell(2).getStringCellValue());
      this.cols.add(c);
    }
  }
  
  /**
   * 根据excel sheet数据初始化列表头index等信息
   * 
   * @param sheet
   */
  public void setColIndex(Sheet sheet) {
    HeaderCell c = new HeaderCell();
    
    int minRowIdx = sheet.getFirstRowNum();
    int maxRowIdx = sheet.getLastRowNum();
    for (int rownum = minRowIdx; rownum < maxRowIdx; rownum++) {
      Row row = sheet.getRow(rownum);
      if(row == null) {
        continue;
      }
      
      int minColIdx = row.getFirstCellNum();
      int maxColIdx = row.getLastCellNum();
      for(int colIdx  =minColIdx; colIdx < maxColIdx; colIdx++) {
        Cell cell = row.getCell(colIdx);
        if(cell == null) {
          continue;
        }
        
        String value = cell.getStringCellValue();
        if (sheet.isColumnHidden(colIdx)) {
          c.setIndex(colIdx);
        }
      }
      this.hiddenCols.add(c);
       
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

  }
  
}
