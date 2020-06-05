package com.github.quanqinle.util.excelutil.parser.impl;

import com.github.quanqinle.util.excelutil.handler.ExcelParserHandler;
import com.github.quanqinle.util.excelutil.param.ParamParser;
import com.github.quanqinle.util.excelutil.parser.ExcelParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

abstract class AbstractExcelParser<T> implements ExcelParser<T> {

    public List<T> parse(ParamParser paramParser) {
        checkParamParser(paramParser);
        ExcelParserHandler<T> handler = this.createHandler(paramParser.getExcelInputStream());
        try {
            return handler.process(paramParser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (paramParser.getExcelInputStream() != null) {
                    paramParser.getExcelInputStream().close();
                }
            } catch (IOException e) {
                // doNothing
            }
        }
    }

    protected abstract ExcelParserHandler<T> createHandler(InputStream excelInputStream);

    /**
     * 校验 参数解析器
     * 
     * @param paramParser
     */
    private void checkParamParser(ParamParser paramParser) {
        if (paramParser == null || paramParser.getExcelInputStream() == null || paramParser.getColumnSize() == null
                || paramParser.getTargetClass() == null || paramParser.getSheetNum() == null) {
            throw new IllegalArgumentException(
                    String.format("ParamParser has null value,ParamParser value is %s", Objects.toString(paramParser)));
        }
    }
}
