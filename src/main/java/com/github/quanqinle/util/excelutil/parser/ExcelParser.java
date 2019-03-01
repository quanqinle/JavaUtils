package com.github.quanqinle.util.excelutil.parser;

import java.util.List;

import com.github.quanqinle.util.excelutil.param.ParserParam;

/**
 * https://www.jianshu.com/p/1038edebfe03
 * 
 * @author quanql
 *
 * @param <T>
 */
public interface ExcelParser<T> {
  List<T> parse(ParserParam parserParam);
}
