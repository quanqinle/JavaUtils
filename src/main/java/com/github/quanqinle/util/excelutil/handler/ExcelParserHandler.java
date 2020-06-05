package com.github.quanqinle.util.excelutil.handler;

import java.util.List;

import com.github.quanqinle.util.excelutil.param.ParamParser;

/**
 * Excel解析器的处理器
 * 
 * @author quanql
 *
 * @param <T>
 */
public interface ExcelParserHandler<T> {

    List<T> process(ParamParser paramParser) throws Exception;

}
