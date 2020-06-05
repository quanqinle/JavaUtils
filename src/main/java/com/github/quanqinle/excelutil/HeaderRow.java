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
 * 横表头（横着排列的，每列的表头） TODO succ
 * 
 * @author quanql
 *
 */
public class HeaderRow {
    /**
     * 表头层级数
     */
    private int multiLevel = 1;
    /**
     * 表头所在的行index，如果是多级表头，则等于表头最后一行 (0-based)
     */
    private int rowIndex = 0;
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
     * 表头单元格列表（可见列）
     */
    private List<HeaderCell> cols = new ArrayList<HeaderCell>();
    /**
     * 表头单元格列表（隐藏列）
     */
    private List<Integer> hiddenColIdxList = new ArrayList<Integer>();

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

    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex
     *            the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * @return the minColIndex
     */
    public int getMinColIndex() {
        return minColIndex;
    }

    /**
     * @param minColIndex
     *            the minColIndex to set
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
     * @param maxColIndex
     *            the maxColIndex to set
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
     * @param minRowIndex
     *            the minRowIndex to set
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
     * @param maxRowIndex
     *            the maxRowIndex to set
     */
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

    /**
     * @return the hiddenColIdxList
     */
    public List<Integer> getHiddenColIdxList() {
        return hiddenColIdxList;
    }

    /**
     * @param hiddenColIdxList
     *            the hiddenColIdxList to set
     */
    public void setHiddenColIdxList(List<Integer> hiddenColIdxList) {
        this.hiddenColIdxList = hiddenColIdxList;
    }

    public HeaderRow() {
    }

    /**
     * 
     * @param multiLevel
     * @param minRowIndex
     * @param maxRowIndex
     * @param minColIndex
     * @param maxColIndex
     */
    public HeaderRow(int multiLevel, int minRowIndex, int maxRowIndex, int minColIndex, int maxColIndex) {
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
        Sheet sheet = workbook.getSheet("横表头");
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
            this.cols.add(c);
        }
    }

    /**
     * 根据excel sheet数据初始化列表头index等信息
     * 
     * @param sheet
     */
    public void setColsIndex(Sheet sheet) {
        boolean foundFirstCell = false;

        int minRowIdx = Math.min(sheet.getFirstRowNum(), getMinRowIndex());
        int maxRowIdx = Math.max(sheet.getLastRowNum(), getMaxRowIndex());
        for (int rowIdx = minRowIdx; rowIdx < maxRowIdx; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
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

                    String value = StringUtils.deleteWhitespace(ReadExcel.getValue(cell));

                    if (foundPreCell && headerCell.isMe(value)) {
                        headerCell.setIndex(colIdx);
                        this.setRowIndex(Math.max(this.getRowIndex(), cell.getRowIndex()));
                        if (!foundFirstCell) { // reduce to traverse row
                            maxRowIdx = rowIdx + this.multiLevel;
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
