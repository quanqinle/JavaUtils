package com.github.quanqinle.readexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.StringUtils;
import com.github.quanqinle.readexcel.entity.ContactExcelData;
import org.apache.poi.hssf.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author quanqinle
 * @date
 */
public class ContactUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.github.quanqinle.readexcel.ContactUtil.class);

    private ContactUtil() {

    }

    /**
     * 校验当前行的必填项
     *
     * @param context
     * @param errorColumnIndexSet
     * @return
     */
    public static void checkRequiredFields(AnalysisContext context, Set<Integer> errorColumnIndexSet) {

        checkNotEmpty("发函单位（客户）*", context, errorColumnIndexSet);
        checkNotEmpty("被函单位*", context, errorColumnIndexSet);

    }

    /**
     * 数值有效性校验
     *
     * @param contactExcelData
     * @param context
     * @param errorColumnIndexSet
     */
    public static void checkValidity(ContactExcelData contactExcelData, AnalysisContext context, Set<Integer> errorColumnIndexSet) {

        checkFieldValueInStringMap(contactExcelData, "sendType", Constants.SEND_TYPE_MAP, context, errorColumnIndexSet);
        checkFieldValueInStringMap(contactExcelData, "sampleFeature", Constants.SAMPLE_FEATURE_MAP, context, errorColumnIndexSet);

        checkFieldValueInStringMap(contactExcelData, "dealType", Constants.DEAL_TYPE_MAP, context, errorColumnIndexSet);
        checkFieldValueInStringMap(contactExcelData, "subject", Constants.CONTACT_SUBJECT_MAP, context, errorColumnIndexSet);
    }

    /**
     *
     * @param obj
     * @param fieldName
     * @param map
     * @param context
     * @param errorColumnIndexSet
     */
    public static void checkFieldValueInStringMap(Object obj, String fieldName, Map map, AnalysisContext context, Set<Integer> errorColumnIndexSet) {

        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            boolean bSetAccess = false;

            String fieldValue = null;
            try {
                fieldValue = (String) field.get(obj);
            } catch (IllegalAccessException e) {
                bSetAccess = true;
                field.setAccessible(true);
//                e.printStackTrace();
            }

//            LOGGER.debug("--field--");
//            LOGGER.debug("name = {}", field.getName());
//            LOGGER.debug("value = {}", fieldValue);
//            LOGGER.debug("Annotations = {}", field.getDeclaredAnnotations());

            if (!bSetAccess) {
                field.setAccessible(false);
            }

            if (!StringUtils.isEmpty(fieldValue)) {
                if (!map.containsKey(fieldValue.trim())) {

                    int columnIndex = getColumnIndexByFieldName(context, fieldName);

                    errorColumnIndexSet.add(columnIndex);
                }
            }
        } catch (NoSuchFieldException e) {
            LOGGER.error("无属性：{}", fieldName);
            e.printStackTrace();
        }

    }

    /**
     * 获取列index
     *
     * @param context
     * @param fieldName 字段在类class中的属性名
     * @return -1表示未查到
     */
    public static int getColumnIndexByFieldName(AnalysisContext context, String fieldName) {

        Map<Integer,Head> headMap = context.currentReadHolder().excelReadHeadProperty().getHeadMap();
        for (Integer colIdx: headMap.keySet()) {
            Head head = headMap.get(colIdx);
            if (fieldName.equalsIgnoreCase(head.getFieldName())) {
                return colIdx;
            }
        }

        return -1;
    }

    /**
     * 判断字段是否非空
     *
     * @param columnIndex
     * @param context
     * @param columnIndexSet 如校验不通过，则将列index加入集合
     * @return 如非空，通过校验，则 true
     */
    private static boolean checkNotEmpty(Integer columnIndex, AnalysisContext context, Set<Integer> columnIndexSet) {
        Cell cell = context.readRowHolder().getCellMap().get(columnIndex);

        if (cell != null) {
            if (CellDataTypeEnum.EMPTY != ((CellData<?>) cell).getType()) {
                return true;
            }
        }

        columnIndexSet.add(columnIndex);
        LOGGER.debug("必填项不应为空：columnIndex={}", columnIndex);

        return false;
    }

    /**
     * 判断字段是否非空
     *
     * @param columnHeadName 列名，如，”姓名“、”年龄“
     * @param context
     * @param columnIndexSet 如校验不通过，则将列index加入集合
     * @return 如非空，通过校验，则 true
     */
    private static boolean checkNotEmpty(String columnHeadName, AnalysisContext context, Set<Integer> columnIndexSet) {

        boolean flag = false;

        ReadHolder readHolder = context.currentReadHolder();
        Map<Integer, Head> headMap = readHolder.excelReadHeadProperty().getHeadMap();

        ReadRowHolder readRowHolder = context.readRowHolder();
        int rowIndex = readRowHolder.getRowIndex();

        for (Integer colIdx: headMap.keySet()) {
            if (headMap.get(colIdx).getHeadNameList().contains(columnHeadName)) {
                if (checkNotEmpty(colIdx, context, columnIndexSet)) {
                    flag = true;
                } else {
                    columnIndexSet.add(colIdx);
                    LOGGER.error("必填项不应为空：第{}行，列名[{}]", rowIndex+1, columnHeadName);
                }
            }
        }
        
        //fixme
        return flag;
    }

    /**
     * 列index转列头符号，如A,B..AC, etc.
     *
     * @param rowIndex
     * @param colIndexSet
     * @param errorMsgList
     */
    public static void convertColIdxToColHead (Integer rowIndex, Set<Integer> colIndexSet, Map<Integer, List<String>> errorMsgList){
        //为什么要加1？ 是为了和文档中的行数对应
        rowIndex = rowIndex + 1;
        List<Integer> errorColumnList = colIndexSet.stream().collect(Collectors.toList());
        //列从小到大排序
        Collections.sort(errorColumnList);
        //加入错误信息中
        for(Integer columnIndex:errorColumnList){
            List<String> list = errorMsgList.get(rowIndex);
            if(list==null){
                list=new LinkedList<>();
                errorMsgList.put(rowIndex,list);
            }

            String columnLetter = CellReference.convertNumToColString(columnIndex);

            if(!list.contains(columnLetter)) {
                list.add(columnLetter);
            }
        }
    }

    /**
     * 把列号（如，0,1…123…，从0开始）转成excel显示的列头（如，A,B…AN…）
     * poi中已有这个函数： CellReference.convertNumToColString(columnIndex);
     *
     * @param columnIndex
     * @return
     */
    public static String convertColIndexToColLetter(Integer columnIndex) {
        String start = "";
        //为了计算算AA  BA CA
        for(Integer count=columnIndex/26; count>0; count=count / 26) {
            start = "" + (char) (count - 1 + 'A');
        }

        //最后一位
        columnIndex = (columnIndex) % 26;
        return start + (char)(columnIndex+'A');
    }

    /**
     * for debug
     * @param args
     */
    public static void main(String[] args) {
        String sample = "异常";
        String[] sampleFeatures = new String[]{"大额","异常","账龄长","零账户","随机","有纠纷","重要客户"};
        boolean contains = Arrays.stream(sampleFeatures).anyMatch(sample.trim()::equals);
        LOGGER.info("是否包含{}", contains);

        int columnNumber = 1;

        int columnIndex = columnNumber - 1;
        String columnLetter = CellReference.convertNumToColString(columnIndex);
        LOGGER.info("idx={}, num={}, letter={}", columnIndex, columnNumber, columnLetter);

        columnLetter = convertColIndexToColLetter(columnIndex);
        LOGGER.info("idx={}, num={}, letter={}", columnIndex, columnNumber, columnLetter);

        Class aClass = ContactExcelData.class;
        Field[] fields = aClass.getFields();

        for (Field f: fields) {
            LOGGER.info("{}-->{}-->{}",f.getName(),f.getType(),f.getAnnotations());
        }

    }

}
