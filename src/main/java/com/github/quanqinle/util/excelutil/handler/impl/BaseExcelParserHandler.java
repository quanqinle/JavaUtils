package com.github.quanqinle.util.excelutil.handler.impl;

import com.github.quanqinle.util.LogUtil;
import com.github.quanqinle.util.excelutil.handler.ExcelParserHandler;
import com.github.quanqinle.util.excelutil.meta.ExcelField;
import com.github.quanqinle.util.excelutil.param.ParamParser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

abstract class BaseExcelParserHandler<T> implements ExcelParserHandler<T> {

    private boolean head = true;

    <T> Optional<T> parseRowToTarget(ParamParser paramParser, List<String> rowData) {
        if (isRowDataEmpty(rowData)) {
            return Optional.empty();
        }

        try {
            T t = doParseRowToTarget(rowData, paramParser.getTargetClass());
            return Optional.of(t);
        } catch (Exception e) {
            LogUtil.error("AbstractExcelParser - parseRowToTarget" + e);
            return Optional.empty();
        }
    }

    private <T> T doParseRowToTarget(List<String> rowData, Class targetClass)
            throws IllegalAccessException, InstantiationException {
        T object = (T) targetClass.newInstance();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ExcelField.class)) {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                int index = excelField.index();
                ExcelField.ExcelFieldType type = excelField.type();
                field.setAccessible(true);
                String setValue = rowData.get(index);
                String toSet = type.buildSetString(setValue);
                field.set(object, toSet);
            }
        }
        return object;
    }

    List<String> initRowList(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add("");
        return list;
    }

    boolean isRowDataEmpty(List<String> rowData) {
        if (rowData == null)
            return true;
        for (String str : rowData) {
            if (str != null && !Objects.equals("", str.trim())) {
                return false;
            }
        }
        return true;
    }

    void validHeader(ParamParser paramParser, List<String> rowData) {
        int index = 0;
        if (rowData.size() != paramParser.getHeader().size()) {
            throw new IllegalArgumentException("Excel Header Check Failed");
        }
        for (String head : paramParser.getHeader()) {
            if (!Objects.equals(rowData.get(index++), head.trim())) {
                throw new IllegalArgumentException("Excel Header Check Failed");
            }
        }
    }

    protected void handleEndOfRow(ParamParser paramParser, List<String> rowData, List<T> result) {
        boolean empty = isRowDataEmpty(rowData);
        if (!empty) {
            if (head && paramParser.getHeader() != null && paramParser.getHeader().size() != 0) {
                validHeader(paramParser, rowData);
                head = false;
            } else {
                Optional<T> t = parseRowToTarget(paramParser, rowData);
                t.ifPresent(result::add);
            }
        }
    }
}
