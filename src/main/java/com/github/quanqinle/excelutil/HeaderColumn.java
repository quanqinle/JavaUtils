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
 * 列的表头（横着排列的）
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
	 * 表头的行index，如果是多级表头，则是表头最后一行
	 */
	private int rowIndex = 0;
	/**
	 * min index of column range
	 */
	private int minColIndex = 0;
	/**
	 * max index of column range
	 */
	private int maxColIndex = 9999;
	/**
	 * min index of row range
	 */
	private int minRowIndex = 0;
	/**
	 * max index of row range
	 */
	private int maxRowIndex = 9999;
	/**
	 * 表头单元格列表（可见列，与hiddenCols相对）
	 */
	private List<HeaderCell> cols = null;
	/**
	 * 表头单元格列表（隐藏列）
	 */
	private List<Integer> hiddenColIdxList = null;

	/**
	 * @return the multiLevel
	 */
	public int getMultiLevel() {
		return multiLevel;
	}
	/**
	 * @param multiLevel
	 *            the multiLevel to set
	 */
	public void setMultiLevel(int multiLevel) {
		this.multiLevel = multiLevel;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public int getMinColIndex() {
		return minColIndex;
	}

	public void setMinColIndex(int minColIndex) {
		this.minColIndex = minColIndex;
	}

	public int getMaxColIndex() {
		return maxColIndex;
	}

	public void setMaxColIndex(int maxColIndex) {
		this.maxColIndex = maxColIndex;
	}
	public int getMinRowIndex() {
		return minRowIndex;
	}
	public void setMinRowIndex(int minRowIndex) {
		this.minRowIndex = minRowIndex;
	}
	public int getMaxRowIndex() {
		return maxRowIndex;
	}
	public void setMaxRowIndex(int maxRowIndex) {
		this.maxRowIndex = maxRowIndex;
	}
	/**
	 * @return the cols
	 */
	public List<HeaderCell> getCols() {
		return cols;
	}

	/**
	 * @param cols
	 *            the cols to set
	 */
	public void setCols(List<HeaderCell> cols) {
		this.cols = cols;
	}

	public HeaderColumn() {
	}
	
	/**
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
	public void setColsByTemplate(Workbook workbook) {
		Sheet sheet = workbook.getSheet("列表头");
		int header = 2;
		for (Row row : sheet) {
			if (header > 0) { // 跳过表头
				header--;
				continue;
			}
			String name = row.getCell(0).getStringCellValue();
			String alias = row.getCell(1).getStringCellValue();
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(alias)) {
				continue;
			}
			HeaderCell c = new HeaderCell();
			c.setName(name);
			c.setAliasName(alias);
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
		boolean foundFirstCell = false;

		int minRowIdx = Math.min(sheet.getFirstRowNum(), getMinRowIndex());
		int maxRowIdx = Math.max(sheet.getLastRowNum(), getMaxRowIndex());
		for (int rownum = minRowIdx; rownum < maxRowIdx; rownum++) {
			Row row = sheet.getRow(rownum);
			if (row == null) {
				continue;
			}

			int firstColIdx = Math.min(row.getFirstCellNum(), getMinColIndex());
			int lastColIdx = Math.max(row.getLastCellNum(), getMaxColIndex());
			
			for (HeaderCell headerCell : cols) {
				String preCellName = headerCell.getPreCellName();
				boolean foundPreCell = StringUtils.isEmpty(preCellName);
				for (int colIdx = firstColIdx; colIdx < lastColIdx; colIdx++) {
					Cell cell = row.getCell(colIdx);
					if (cell == null) {
						continue;
					}

					String value = StringUtils.deleteWhitespace(cell.getStringCellValue());
					
					if (foundPreCell && headerCell.isMe(value)) {
						headerCell.setIndex(colIdx);
						this.setRowIndex(Math.max(this.getRowIndex(), cell.getRowIndex()));
						if (!foundFirstCell) { // reduce to traverse row
							maxRowIdx = rownum + this.multiLevel;
							foundFirstCell = true;
						}
						break;
					}
					if (foundPreCell || headerCell.isMe(preCellName)) {
						foundPreCell = true;
					}
					if (sheet.isColumnHidden(colIdx)) {
						this.hiddenColIdxList.add(colIdx); // fixme duplication
					}
				}
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
	public HeaderCell getCell(String cellname, String preCellName) {
		// verify parameters
		if (StringUtils.isWhitespace(cellname) || StringUtils.isEmpty(cellname)) {
			return null;
		}
		
		boolean findPreCell = false;
		if (StringUtils.isWhitespace(preCellName) || StringUtils.isEmpty(preCellName)) {
			findPreCell = true;
		}
		for (HeaderCell headerCell : this.cols) {
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
