package com.github.quanqinle.readexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.RowTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.fastjson.JSON;
import com.github.quanqinle.readexcel.entity.ContactExcelData;
import com.github.quanqinle.readexcel.entity.ContactExcelResult;
import com.github.quanqinle.readexcel.exception.ExcelHeadException;
import org.apache.poi.hssf.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.alibaba.excel.enums.CellExtraTypeEnum.MERGE;

/**
 *
 * @author quanqinle
 */
public class ContactListener extends AnalysisEventListener<ContactExcelData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.github.quanqinle.readexcel.ContactListener.class);

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;

    /**
     * 导入结果。
     * 只能通过构造函数传入，获得处理结果
     */
    private ContactExcelResult contactResult;

    /**
     * 表头行/列
     */
    Map<Integer, Map<Integer, CellData>> rowIdx2HeadMap = new HashMap<>();
    /**
     * 有效行/列信息
     */
    private Map<Integer, ContactExcelData> rowIdx2RowDataMap = new HashMap<>();
    /**
     * 当前行的错误列index集合
     */
    private Set<Integer> currentRowErrorColumnIndexSet;
    /**
     * 错误行/列信息
     */
    private Map<Integer, Set<Integer>> rowIdx2ErrColIdxMap = new HashMap<>();

    private ContactListener() {
        super();
        contactResult = new ContactExcelResult();
    }

    public ContactListener(ContactExcelResult contactResult) {
        this.contactResult = contactResult;
    }

    public ContactListener(Map<Integer, Map<Integer, CellData>> rowIdx2HeadMap, Map<Integer, ContactExcelData> rowIdx2RowDataMap, Map<Integer, Set<Integer>> rowIdx2ErrColIdxMap) {
        this.rowIdx2HeadMap = rowIdx2HeadMap;
        this.rowIdx2RowDataMap = rowIdx2RowDataMap;
        this.rowIdx2ErrColIdxMap = rowIdx2ErrColIdxMap;
    }

    /**
     * 处理表头
     * 一行一行调用该函数
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);

        ReadRowHolder readRowHolder = context.readRowHolder();
        int rowIndex = readRowHolder.getRowIndex();

        RowTypeEnum rowType = readRowHolder.getRowType();
        LOGGER.debug("Head: 行号={}, RowIndex={}, Type={}, map={}", rowIndex+1, rowIndex, rowType, JSON.toJSONString(headMap));

        rowIdx2HeadMap.put(rowIndex, headMap);

        /**
         * 表头合法性校验
         */
        if (ContactExcelData.HEAD_ROW_NUMBER == rowIndex + 1) {
            for (Integer key : ContactExcelData.HEAD_CHECK_MAP.keySet()) {
                String expect = ContactExcelData.HEAD_CHECK_MAP.get(key).trim();
                CellData cell = headMap.get(key);
                if (null == cell) {
                    //模板不符！退出
                    throw new ExcelHeadException("表头与预期不符。未找到表头：" + expect);
                }

                String real = cell.getStringValue();
                real = (real==null? null : real.trim());
                if (!expect.equalsIgnoreCase(real)) {
                    //模板不符！退出
                    throw new ExcelHeadException("表头与预期不符。期望：" + expect + " <--> 实际：" + real);
                }
            }
        }

    }

    /**
     * When analysis one row trigger invoke function.
     * 自动跳过空行，即，空行不会进入这个函数
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context -
     */
    @Override
    public void invoke(ContactExcelData data, AnalysisContext context) {

        currentRowErrorColumnIndexSet = new TreeSet<>();

        ReadRowHolder readRowHolder = context.readRowHolder();
        int rowIndex = readRowHolder.getRowIndex();
        RowTypeEnum rowType = readRowHolder.getRowType();
        Map<Integer, Cell> cellMap = readRowHolder.getCellMap();

        LOGGER.debug("Data: 行号={}, RowIndex={}, Type={}, map={}", rowIndex+1, rowIndex, rowType, JSON.toJSONString(cellMap));

        ContactUtil.checkRequiredFields(context, currentRowErrorColumnIndexSet);
        ContactUtil.checkValidity(data, context, currentRowErrorColumnIndexSet);

        if (currentRowErrorColumnIndexSet.isEmpty()) {
            rowIdx2RowDataMap.put(rowIndex, data);
        } else {
            Set<Integer> errColIdxMap = rowIdx2ErrColIdxMap.get(rowIndex);
            if (errColIdxMap != null) {
                currentRowErrorColumnIndexSet.addAll(errColIdxMap);
            }
            rowIdx2ErrColIdxMap.put(rowIndex, currentRowErrorColumnIndexSet);
        }

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (rowIdx2RowDataMap.size() >= BATCH_COUNT) {
//            saveData();
            // 存储完成，清理list
//            list.clear();
        }
    }

    /**
     * if have something to do after all analysis
     * 所有数据解析完成了！（都会来调用）
     *
     * @param context -
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
//        saveData();

        contactResult.setRowIdx2HeadMap(rowIdx2HeadMap);

        contactResult.setRowIdx2ErrColIdxMap(rowIdx2ErrColIdxMap);

        contactResult.setRowIdx2RowDataMap(rowIdx2RowDataMap);

    }

    /**
     * The current method is called when extra information is returned
     *
     * @param extra   extra information
     * @param context -
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        super.extra(extra, context);
        LOGGER.debug("读取到了一条额外信息:{}", JSON.toJSONString(extra));
        if (MERGE == extra.getType()) {
            LOGGER.debug(
                    "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                    extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                    extra.getLastColumnIndex());
        }
    }

    /**
     * All listeners receive this method when any one Listener does an error report. If an exception is thrown here, the
     * entire read will terminate.
     * 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception -
     * @param context -
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {

        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            Integer cellRowIndex = excelDataConvertException.getRowIndex();
            Integer cellColumnIndex = excelDataConvertException.getColumnIndex();

            String cellColumnString = CellReference.convertNumToColString(cellColumnIndex);
            LOGGER.error("第{}行{}列，数值转换异常：{}", cellRowIndex+1, cellColumnString, exception.getMessage());

            Set<Integer> errColIdxMap = rowIdx2ErrColIdxMap.get(cellRowIndex);
            if (errColIdxMap == null) {
                errColIdxMap = new TreeSet<>();
            }
            errColIdxMap.add(cellColumnIndex);
            rowIdx2ErrColIdxMap.put(cellRowIndex, errColIdxMap);
        } else if (exception instanceof ExcelHeadException) {

            LOGGER.error(exception.getMessage());

            // 表格不符合规范，抛出异常，触发终止解析
            throw exception;
        } else {
            LOGGER.error("第{}行解析失败，但是继续解析下一行。exception: \n{}",
                    context.readRowHolder().getRowIndex() + 1,
                    Arrays.toString(exception.getStackTrace()).replaceAll(",", "\n"));
            /*exception.getMessage()*/
        }
    }


    public static void main(String[] args) {

        Map<Integer, Set<Integer>> rowIdx2ErrColIdxMap = new HashMap<>();
        int cellRowIndex = 1;
        int cellColumnIndex = 2;

        Set<Integer> errColIdxMap = rowIdx2ErrColIdxMap.get(cellRowIndex);
        if (errColIdxMap == null) {
            errColIdxMap = new TreeSet<>();
        }
        errColIdxMap.add(cellColumnIndex);
        rowIdx2ErrColIdxMap.put(cellRowIndex, errColIdxMap);

        ContactExcelData contactExcelData = new ContactExcelData();
        contactExcelData.setCompany("ms");
        contactExcelData.setNoInvoiceAccrual("12.34");
//        ConfirmationPo confirmationPo = ExcelDataMapConfirmationPO.INSTANCE.sourceToTarget(contactExcelData);
//        LOGGER.info("map to {}", confirmationPo);
    }

}
