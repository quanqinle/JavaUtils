package com.github.quanqinle.util.excelutil.parser.impl;

import com.github.quanqinle.util.excelutil.handler.impl.ExcelDomParserHandler;
import com.github.quanqinle.util.excelutil.handler.ExcelParserHandler;

import java.io.InputStream;

public class ExcelDomParser<T> extends AbstractExcelParser<T> {

    private ExcelParserHandler<T> excelParserHandler;

    public ExcelDomParser() {
        this.excelParserHandler = new ExcelDomParserHandler<>();
    }

    @Override
    protected ExcelParserHandler<T> createHandler(InputStream excelInputStream) {
        return this.excelParserHandler;
    }
}
