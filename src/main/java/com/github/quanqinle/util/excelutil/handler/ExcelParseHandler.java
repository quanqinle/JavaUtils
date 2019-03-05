package com.github.quanqinle.util.excelutil.handler;

import java.util.List;

import com.github.quanqinle.util.excelutil.param.ParserParam;

public interface ExcelParseHandler<T> {

	List<T> process(ParserParam parserParam) throws Exception;

}
