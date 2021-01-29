package com.github.quanqinle.readexcel.entity;

import com.alibaba.excel.metadata.CellData;
import lombok.Data;
import org.apache.poi.hssf.util.CellReference;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * contact导入结果
 * @author quanqinle
 */
@Data
public class ContactExcelResult {

    /**
     * 表头map。
     * 行index->表头整行
     */
    Map<Integer, Map<Integer, CellData>> rowIdx2HeadMap;

    /**
     * 有效数据map。
     * 行index->数据整行
     */
    Map<Integer, ContactExcelData> rowIdx2RowDataMap;

    /**
     * 错误行列map。
     * 行index->错误列index
     */
    Map<Integer, Set<Integer>> rowIdx2ErrColIdxMap;

    /**
     * 错误行列map。
     * 行号->错误列的列名
     * 可以通过 genRowNum2ErrColNameMap() 填充数据
     */
    Map<Integer, Set<String>> rowNum2ErrColNameMap;

    /**
     * 根据 rowIdx2ErrColIdxMap 生成 rowNum2ErrColNameMap
     * 错误列号转列名称，如，1-->A
     *
     * @return rowIdx2ErrColIdxMap空时，返回null；否则，返回转换后的值
     */
    public Map<Integer, Set<String>> genRowNum2ErrColNameMap(Map<Integer, Set<Integer>> rowIdx2ErrColIdxMap) {

        if (rowIdx2ErrColIdxMap == null || 0 == rowIdx2ErrColIdxMap.size()) {
            return null;
        }

        Map<Integer, Set<String>> rowNum2ErrColNameMap = new TreeMap<>();

        for (Integer rowIdx: rowIdx2ErrColIdxMap.keySet()) {
            Set<Integer> errColIdxMap = rowIdx2ErrColIdxMap.get(rowIdx);

            Set<String> errColNameSet = new TreeSet<>();
            for (Integer colIdx: errColIdxMap) {
                errColNameSet.add(CellReference.convertNumToColString(colIdx));
            }

            rowNum2ErrColNameMap.put(rowIdx + 1, errColNameSet);
        }

        this.rowIdx2ErrColIdxMap = rowIdx2ErrColIdxMap;

        return rowNum2ErrColNameMap;
    }

}
