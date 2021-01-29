package com.github.quanqinle.readexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.listener.ModelBuildEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.ConverterUtils;
import net.sf.cglib.beans.BeanMap;

import java.util.*;

/**
 * @author quanqinle
 */
public class MyModelBuildEventListener extends
//AbstractIgnoreExceptionReadListener<Map<Integer, CellData>> {
        ModelBuildEventListener {

    /**
     * When analysis one head row trigger invoke function.
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {

    }

    /**
     * if have something to do after all analysis
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public void invoke(Map<Integer, CellData> cellDataMap, AnalysisContext context) {
        ReadHolder currentReadHolder = context.currentReadHolder();
        if (HeadKindEnum.CLASS.equals(currentReadHolder.excelReadHeadProperty().getHeadKind())) {
            context.readRowHolder()
                    .setCurrentRowAnalysisResult(buildUserModel(cellDataMap, currentReadHolder, context));
            return;
        }
        context.readRowHolder().setCurrentRowAnalysisResult(buildStringList(cellDataMap, currentReadHolder, context));
    }

    private Object buildStringList(Map<Integer, CellData> cellDataMap, ReadHolder currentReadHolder,
                                   AnalysisContext context) {
        int index = 0;
        if (context.readWorkbookHolder().getDefaultReturnMap()) {
            Map<Integer, String> map = new LinkedHashMap<Integer, String>(cellDataMap.size() * 4 / 3 + 1);
            for (Map.Entry<Integer, CellData> entry : cellDataMap.entrySet()) {
                Integer key = entry.getKey();
                CellData cellData = entry.getValue();
                while (index < key) {
                    map.put(index, null);
                    index++;
                }
                index++;
                if (cellData.getType() == CellDataTypeEnum.EMPTY) {
                    map.put(key, null);
                    continue;
                }
                map.put(key,
                        (String) ConverterUtils.convertToJavaObject(cellData, null, null, currentReadHolder.converterMap(),
                                currentReadHolder.globalConfiguration(), context.readRowHolder().getRowIndex(), key));
            }
            int headSize = currentReadHolder.excelReadHeadProperty().getHeadMap().size();
            while (index < headSize) {
                map.put(index, null);
                index++;
            }
            return map;
        } else {
            // Compatible with the old code the old code returns a list
            List<String> list = new ArrayList<String>();
            for (Map.Entry<Integer, CellData> entry : cellDataMap.entrySet()) {
                Integer key = entry.getKey();
                CellData cellData = entry.getValue();
                while (index < key) {
                    list.add(null);
                    index++;
                }
                index++;
                if (cellData.getType() == CellDataTypeEnum.EMPTY) {
                    list.add(null);
                    continue;
                }
                list.add(
                        (String) ConverterUtils.convertToJavaObject(cellData, null, null, currentReadHolder.converterMap(),
                                currentReadHolder.globalConfiguration(), context.readRowHolder().getRowIndex(), key));
            }
            int headSize = currentReadHolder.excelReadHeadProperty().getHeadMap().size();
            while (index < headSize) {
                list.add(null);
                index++;
            }
            return list;
        }
    }

    private Object buildUserModel(Map<Integer, CellData> cellDataMap, ReadHolder currentReadHolder,
                                  AnalysisContext context) {
        ExcelReadHeadProperty excelReadHeadProperty = currentReadHolder.excelReadHeadProperty();
        Object resultModel;
        try {
            resultModel = excelReadHeadProperty.getHeadClazz().newInstance();
        } catch (Exception e) {
            throw new ExcelDataConvertException(context.readRowHolder().getRowIndex(), 0,
                    new CellData(CellDataTypeEnum.EMPTY), null,
                    "Can not instance class: " + excelReadHeadProperty.getHeadClazz().getName(), e);
        }
        Map<Integer, Head> headMap = excelReadHeadProperty.getHeadMap();
        Map<String, Object> map = new HashMap<String, Object>(headMap.size() * 4 / 3 + 1);
        Map<Integer, ExcelContentProperty> contentPropertyMap = excelReadHeadProperty.getContentPropertyMap();
        LinkedList<ExcelDataConvertException> exceptionLinkedList = new LinkedList<>();
        for (Map.Entry<Integer, Head> entry : headMap.entrySet()) {
            Integer index = entry.getKey();
            if (!cellDataMap.containsKey(index)) {
                continue;
            }
            CellData cellData = cellDataMap.get(index);
            if (cellData.getType() == CellDataTypeEnum.EMPTY) {
                continue;
            }
            ExcelContentProperty excelContentProperty = contentPropertyMap.get(index);
            try {
                Object value = ConverterUtils.convertToJavaObject(cellData, excelContentProperty.getField(),
                        excelContentProperty, currentReadHolder.converterMap(), currentReadHolder.globalConfiguration(),
                        context.readRowHolder().getRowIndex(), index);
                if (value != null) {
                    map.put(excelContentProperty.getField().getName(), value);
                }
            } catch (ExcelDataConvertException e) {
                exceptionLinkedList.add(e);
            }
        }
        //没有异常转换为需要的map
        if (CollectionUtils.isEmpty(exceptionLinkedList)) {
            BeanMap.create(resultModel).putAll(map);
            return resultModel;
        } else {
            //存在异常，挨个抛出，最后一个异常往外抛结束运行
            for (int i = 0; i < exceptionLinkedList.size(); i++) {
                ExcelDataConvertException exception = exceptionLinkedList.get(i);
                if (i < exceptionLinkedList.size() - 1) {
                    handleLastException(context, exception);
                } else {
                    throw exception;
                }
            }
            return null;
        }

    }

    private void handleLastException(AnalysisContext analysisContext, Exception e) {
        for (ReadListener readListenerException : analysisContext.currentReadHolder().readListenerList()) {
            try {
                readListenerException.onException(e, analysisContext);
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e1) {
                throw new ExcelAnalysisException(e1.getMessage(), e1);
            }
        }
    }


}
