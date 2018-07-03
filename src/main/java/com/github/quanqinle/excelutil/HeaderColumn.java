/**
 * 
 */
package com.github.quanqinle.excelutil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.quanqinle.util.ReadExcel;

/**
 * 竖表头（竖着排列的，每行的表头）
 * FIXME
 * @author quanql
 *
 */
public class HeaderColumn {
  /**
   * 表头层级数
   */
  private int multiLevel = 1;
  /**
   * 表头所在的列index，如果是多级表头，则等于表头最后一列 (0-based)
   */
  private int columnIndex = 0;
  /**
   * min index of column range (0-based)
   */
  private int minColIndex = 0;
  /**
   * max index of column range (0-based)
   */
  private int maxColIndex = 9999;
  /**
   * min index of row range (0-based)
   */
  private int minRowIndex = 0;
  /**
   * max index of row range (0-based)
   */
  private int maxRowIndex = 9999;
  /**
   * 表头单元格列表（可见行）
   */
  private List<HeaderCell> rows = new ArrayList<HeaderCell>();
  /**
   * 表头单元格列表（隐藏行）
   */
  private List<Integer> hiddenRowIdxList = new ArrayList<Integer>();

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
   * @return the columnIndex
   */
  public int getColumnIndex() {
    return columnIndex;
  }

  /**
   * @param columnIndex the columnIndex to set
   */
  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }

  /**
   * @return the minColIndex
   */
  public int getMinColIndex() {
    return minColIndex;
  }

  /**
   * @param minColIndex the minColIndex to set
   */
  public void setMinColIndex(int minColIndex) {
    this.minColIndex = minColIndex;
  }

  /**
   * @return the maxColIndex
   */
  public int getMaxColIndex() {
    return maxColIndex;
  }

  /**
   * @param maxColIndex the maxColIndex to set
   */
  public void setMaxColIndex(int maxColIndex) {
    this.maxColIndex = maxColIndex;
  }

  /**
   * @return the minRowIndex
   */
  public int getMinRowIndex() {
    return minRowIndex;
  }

  /**
   * @param minRowIndex the minRowIndex to set
   */
  public void setMinRowIndex(int minRowIndex) {
    this.minRowIndex = minRowIndex;
  }

  /**
   * @return the maxRowIndex
   */
  public int getMaxRowIndex() {
    return maxRowIndex;
  }

  /**
   * @param maxRowIndex the maxRowIndex to set
   */
  public void setMaxRowIndex(int maxRowIndex) {
    this.maxRowIndex = maxRowIndex;
  }

  /**
   * @return the rows
   */
  public List<HeaderCell> getRows() {
    return rows;
  }

  /**
   * @param rows the rows to set
   */
  public void setRows(List<HeaderCell> rows) {
    this.rows = rows;
  }

  /**
   * @return the hiddenRowIdxList
   */
  public List<Integer> getHiddenRowIdxList() {
    return hiddenRowIdxList;
  }

  /**
   * @param hiddenRowIdxList the hiddenRowIdxList to set
   */
  public void setHiddenRowIdxList(List<Integer> hiddenRowIdxList) {
    this.hiddenRowIdxList = hiddenRowIdxList;
  }

  public HeaderColumn() {
  }
  
  /**
   * 
   * @param multiLevel
   * @param minRowIndex
   * @param maxRowIndex
   * @param minColIndex
   * @param maxColIndex
   */
  public HeaderColumn(int multiLevel, int minRowIndex, int maxRowIndex, int minColIndex, int maxColIndex) {
    super();
    this.multiLevel = multiLevel;
    this.minRowIndex = minRowIndex;
    this.maxRowIndex = maxRowIndex;
    this.minColIndex = minColIndex;
    this.maxColIndex = maxColIndex;
  }
  /**
   * 根据excel模板初始化列表头name等信息
   * 
   * @param workbook
   */
  public void setRowsByTemplate(Workbook workbook) {
    Sheet sheet = workbook.getSheet("竖表头");
    int header = 2;
    for (Row row : sheet) {
      if (header > 0) { // 跳过表头
        header--;
        continue;
      }
      String name = ReadExcel.getValue(row.getCell(0));
      String alias = ReadExcel.getValue(row.getCell(1));
      if (StringUtils.isEmpty(name) || StringUtils.isEmpty(alias)) {
        continue;
      }
      HeaderCell c = new HeaderCell();
      c.setName(name);
      c.setAliasName(alias);
      c.setPreCellName(ReadExcel.getValue(row.getCell(2)));
      this.rows.add(c);
    }
  }

  /**
   * 设置部分表头的index（即，没有约束前辈节点的表头）
   * 
   * @param sheet
   */
  protected void setColsIndexNoPreCell(Sheet sheet) {
    boolean foundFirstCell = false;
    int minColIdx = getMinColIndex();
    int maxColIdx = getMaxColIndex();
    int minRowIdx = Math.min(sheet.getFirstRowNum(), getMinRowIndex());
    int maxRowIdx = Math.min(sheet.getLastRowNum(), getMaxRowIndex());
    
    for (int colIdx = minColIdx; colIdx < maxColIdx; colIdx++) {
      for (int rowIdx = minRowIdx; rowIdx < maxRowIdx; rowIdx++) {
        Row row = sheet.getRow(rowIdx);
        if (row == null) {
          continue;
        }
        minColIdx = Math.min(row.getFirstCellNum(), minColIdx);
        maxColIdx = Math.min(row.getLastCellNum(), maxColIdx);
        
        Cell cell = row.getCell(colIdx);
        if (cell == null) {
          continue;
        }
        String value = StringUtils.deleteWhitespace(ReadExcel.getValue(cell));
        if (StringUtils.isEmpty(value)) {
          continue;
        }
        
        for (HeaderCell headerCell : cols) {
          if (headerCell.isMe(value)) {
            headerCell.setIndex(rowIdx);
            this.setColumnIndex(Math.max(this.getColumnIndex(), cell.getColumnIndex()));
            lastColIdx = colIdx + this.multiLevel;
            break;
          }
        }
      } // end row
      
    } // end col
    
  } // end func
  
  /**
   * 根据excel sheet数据初始化列表头index等信息
   * 
   * @param sheet
   */
  public void setRowsIndex(Sheet sheet) {
    boolean foundFirstCell = false;

    int minRowIdx = Math.min(sheet.getFirstRowNum(), getMinRowIndex());
    int maxRowIdx = Math.max(sheet.getLastRowNum(), getMaxRowIndex());
    for (int rowIdx = minRowIdx; rowIdx < maxRowIdx; rowIdx++) {
      Row row = sheet.getRow(rowIdx);
      if (row == null) {
        continue;
      }

      int minColIdx = Math.min(row.getFirstCellNum(), getMinColIndex());
      int maxColIdx = Math.max(row.getLastCellNum(), getMaxColIndex());
      
      for (HeaderCell headerCell : rows) {
        String preCellName = headerCell.getPreCellName();
        boolean foundPreCell = StringUtils.isEmpty(preCellName);
        for (int colIdx = minColIdx; colIdx < maxColIdx; colIdx++) {
          Cell cell = row.getCell(colIdx);
          if (cell == null) {
            continue;
          }

          String value = StringUtils.deleteWhitespace(ReadExcel.getValue(cell));
          
          if (foundPreCell && headerCell.isMe(value)) {
            headerCell.setIndex(rowIdx);
            this.setColumnIndex(Math.max(this.getColumnIndex(), cell.getColumnIndex()));
            if (!foundFirstCell) { // reduce to traverse row
              maxColIdx = colIdx + this.multiLevel;
              foundFirstCell = true;
            }
            break;
          }
          if (foundPreCell || headerCell.isMe(preCellName)) {
            foundPreCell = true;
          }
        }
      }
      if (row.getZeroHeight()) {
        this.hiddenRowIdxList.add(rowIdx); // fixme duplication
      }
    }
  }

  /**
   * 获取表头单元格列表中的Cell，根据cell名称和cell前辈节点名称
   * 
   * @param cellname
   * @param preCellName
   * @return
   */
  protected HeaderCell getCell(String cellname, String preCellName) {
    // verify parameters
    if (StringUtils.isWhitespace(cellname) || StringUtils.isEmpty(cellname)) {
      return null;
    }
    
    boolean findPreCell = false;
    if (StringUtils.isWhitespace(preCellName) || StringUtils.isEmpty(preCellName)) {
      findPreCell = true;
    }
    for (HeaderCell headerCell : this.rows) {
      if (findPreCell || headerCell.isMe(preCellName)) {
        findPreCell = true;
        
      }
      if (findPreCell && headerCell.isMe(cellname)) {
        return headerCell;
      }
    }
    return null;
  }
  /**
   * @param args
   */
  public static void main(String[] args) {

  }

}
