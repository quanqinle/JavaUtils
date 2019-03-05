package com.github.quanqinle.util.excelutil.parser.impl;

import com.github.quanqinle.util.excelutil.handler.impl.ExcelDomParseHandler;
import com.github.quanqinle.util.excelutil.handler.ExcelParseHandler;

import java.io.InputStream;

public class ExcelDomParser<T> extends AbstractExcelParser<T> {

	private ExcelParseHandler<T> excelParseHandler;

	public ExcelDomParser() {
		this.excelParseHandler = new ExcelDomParseHandler<>();
	}

	@Override
	protected ExcelParseHandler<T> createHandler(InputStream excelInputStream) {
		return this.excelParseHandler;
	}
}
