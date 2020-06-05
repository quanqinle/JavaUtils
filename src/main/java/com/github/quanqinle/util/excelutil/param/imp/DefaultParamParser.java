package com.github.quanqinle.util.excelutil.param.imp;

import java.io.InputStream;
import java.util.List;

import com.github.quanqinle.util.excelutil.param.ParamParser;

public class DefaultParamParser implements ParamParser {

    private InputStream inputStream;
    private Class<?> targetClass;
    private Integer columnSize;
    private List<String> header;
    private Integer sheetNum;

    private DefaultParamParser(InputStream inputStream, Class<?> targetClass, Integer columnSize, List<String> header,
            Integer sheetNum) {
        this.inputStream = inputStream;
        this.targetClass = targetClass;
        this.columnSize = columnSize;
        this.sheetNum = sheetNum;
        this.header = header;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public InputStream getExcelInputStream() {
        return inputStream;
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public Integer getColumnSize() {
        return columnSize;
    }

    @Override
    public Integer getSheetNum() {
        return sheetNum;
    }

    @Override
    public List<String> getHeader() {
        return header;
    }

    public static class Builder {

        private InputStream inputStream;
        private Class<?> targetClass;
        private Integer columnSize;
        private List<String> header;
        private Integer sheetNum;

        public Builder excelInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public Builder targetClass(Class<?> clazz) {
            this.targetClass = clazz;
            return this;
        }

        public Builder columnSize(Integer columnSize) {
            this.columnSize = columnSize;
            return this;
        }

        public Builder header(List<String> header) {
            this.header = header;
            return this;
        }

        public Builder sheetNum(Integer sheetNum) {
            this.sheetNum = sheetNum;
            return this;
        }

        public DefaultParamParser build() {
            return new DefaultParamParser(inputStream, targetClass, columnSize, header, sheetNum);
        }
    }
}
