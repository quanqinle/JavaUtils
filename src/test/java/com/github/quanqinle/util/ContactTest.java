package com.github.quanqinle.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.github.quanqinle.readexcel.ContactListener;
import com.github.quanqinle.readexcel.entity.ContactExcelData;
import com.github.quanqinle.readexcel.entity.ContactExcelResult;
import com.github.quanqinle.readexcel.exception.ExcelHeadException;
import com.github.quanqinle.readexcel.listener.MyModelBuildEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author quanqinle
 * @date
 */
public class ContactTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactTest.class);

    public static void main(String[] args) {
        String file;
        file = "D:\\Demo1.xlsx";

        read_1(file);

//        read_2(file);
    }


    public static void read_1(String file) {
        LOGGER.info("-- read_1 --");

        ContactExcelResult contactExcelResult = new ContactExcelResult();

        ExcelReader excelReader = EasyExcel.read(file, ContactExcelData.class, new ContactListener(contactExcelResult))
                .useDefaultListener(false)
                .registerReadListener(new MyModelBuildEventListener())
                .build();
        ReadSheet readSheet = EasyExcel.readSheet(0).headRowNumber(ContactExcelData.HEAD_ROW_NUMBER).build();

        try {
            excelReader.read(readSheet);
        } catch (Exception e) {
            if (e instanceof ExcelHeadException) {
                LOGGER.error("Excel模板错误！请下载并使用最新模板");
            } else {
                LOGGER.error("异常");
            }
        }
//        excelReader.finish();

        LOGGER.info("-- reading excel complete! --");
        Map<Integer, Map<Integer, CellData>> rowIdx2HeadMap = contactExcelResult.getRowIdx2HeadMap();
        Map<Integer, Set<String>> rowNum2ErrColNameMap = contactExcelResult.genRowNum2ErrColNameMap(contactExcelResult.getRowIdx2ErrColIdxMap());
        Map<Integer, ContactExcelData> rowIdx2RowDataMap = contactExcelResult.getRowIdx2RowDataMap();
        for (Integer rowIdx: rowIdx2HeadMap.keySet()) {
            Map<Integer, CellData> colIdx2CellDataMap = rowIdx2HeadMap.get(rowIdx);
            LOGGER.info("表头：{}行，{}", rowIdx+1, JSON.toJSONString(colIdx2CellDataMap));
        }
//        for (Integer rowNum: rowNum2ErrColNameMap.keySet()) {
//            LOGGER.info("错误：第{}行={}", rowNum, JSON.toJSONString(rowNum2ErrColNameMap.get(rowNum)));
//        }
        for (Integer rowIdx: rowIdx2RowDataMap.keySet()) {
            LOGGER.info("数据：{}行，{}", rowIdx+1, JSON.toJSONString(rowIdx2RowDataMap.get(rowIdx)));
        }

/*        Map<Integer, Set<String>> rowNumErrorMsgMap = contactResult.getRowNumErrorMsgMap();
        List<ConfirmationPo> confirmationSaves = contactResult.getConfirmationSaves();
        List<ConfirmationContactPo> confirmationContacts = contactResult.getConfirmationContacts();
        List<ConfirmationContactDealsPo> confirmationContactDeals = contactResult.getConfirmationContactDeals();
        List<ConfirmationContactImportancesPo> confirmationContactImportances = contactResult.getConfirmationContactImportances();

        for (Integer rowIdx: rowIdx2RowDataMap.keySet()) {
            ContactExcelData data = rowIdx2RowDataMap.get(rowIdx);
            LOGGER.info("数据：第{}行={}", rowIdx+1, JSON.toJSONString(data));

            try {
                confirmationSaves.add( ExcelDataMapConfirmationPO.INSTANCE.sourceToTarget(data) );
                confirmationContacts.add(ExcelDataMapConfirmationContactPO.INSTANCE.sourceToTarget(data) );
                confirmationContactDeals.add(ExcelDataMapConfirmationContactDealsPO.INSTANCE.sourceToTarget(data) );
                confirmationContactImportances.add(ExcelDataMapConfirmationContactImportancesPO.INSTANCE.sourceToTarget(data) );

//                LOGGER.info("ConfirmationPo={}", JSON.toJSONString(confirmationPo));
            } catch (RuntimeException e) {
                LOGGER.error("e={}", e.getMessage());
            }

        }

        contactResult.setConfirmationSaves(confirmationSaves);
        contactResult.setConfirmationContacts(confirmationContacts);
        contactResult.setConfirmationContactDeals(confirmationContactDeals);
        contactResult.setConfirmationContactImportances(confirmationContactImportances);

        for (Integer rowNum: rowNumErrorMsgMap.keySet()) {
            LOGGER.info("错误：第{}行={}", rowNum, JSON.toJSONString(rowNumErrorMsgMap.get(rowNum)));
        }
        for (ConfirmationPo confirmation: confirmationSaves) {
            LOGGER.info("数据：{}", JSON.toJSONString(confirmation));
        }*/

    }

    public static void read_2(String file) {
        LOGGER.info("-- read_2 --");
        Map<Integer, Map<Integer, CellData>> rowIdx2HeadMap = new TreeMap<>();
        Map<Integer, ContactExcelData> rowIdx2RowDataMap = new TreeMap<>();
        Map<Integer, Set<Integer>> rowIdx2ErrColIdxMap = new TreeMap<>();
        ContactListener contactListener = new ContactListener(rowIdx2HeadMap, rowIdx2RowDataMap, rowIdx2ErrColIdxMap);
        try {
            EasyExcel.read(file, ContactListener.class, contactListener)
                    .useDefaultListener(false)
                    .registerReadListener(new MyModelBuildEventListener())
//                    .registerReadListener(contactListener)
                    .sheet()
                    .headRowNumber(ContactExcelData.HEAD_ROW_NUMBER)
                    .doRead();
        } catch (Exception e) {
            LOGGER.error("e={}", e);
        }

        for (Integer key: rowIdx2ErrColIdxMap.keySet()) {
            LOGGER.info(key+"="+rowIdx2ErrColIdxMap.get(key));
        }
    }
}
