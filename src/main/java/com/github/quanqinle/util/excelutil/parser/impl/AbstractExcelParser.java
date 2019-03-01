package com.github.quanqinle.util.excelutil.parser.impl;

import com.github.quanqinle.util.excelutil.handler.ExcelParseHandler;
import com.github.quanqinle.util.excelutil.param.ParserParam;
import com.github.quanqinle.util.excelutil.parser.ExcelParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

abstract class AbstractExcelParser<T> implements ExcelParser<T> {

  public List<T> parse(ParserParam parserParam) {
    checkParserParam(parserParam);
    ExcelParseHandler<T> handler = this.createHandler(parserParam.getExcelInputStream());
    try {
      return handler.process(parserParam);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (parserParam.getExcelInputStream() != null) {
          parserParam.getExcelInputStream().close();
        }
      } catch (IOException e) {
        // doNothing
      }
    }
  }

  protected abstract ExcelParseHandler<T> createHandler(InputStream excelInputStream);

  private void checkParserParam(ParserParam parserParam) {
    if (parserParam == null || parserParam.getExcelInputStream() == null || parserParam.getColumnSize() == null
        || parserParam.getTargetClass() == null || parserParam.getSheetNum() == null) {
      throw new IllegalArgumentException(
          String.format("ParserParam has null value,ParserParam value is %s", Objects.toString(parserParam)));
    }
  }
}
