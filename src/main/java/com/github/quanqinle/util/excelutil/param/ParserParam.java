package com.github.quanqinle.util.excelutil.param;

import java.io.InputStream;
import java.util.List;

public interface ParserParam {

  Integer FIRST_SHEET = 0;

  InputStream getExcelInputStream();

  Class<?> getTargetClass();

  Integer getColumnSize();

  Integer getSheetNum();

  List<String> getHeader();
}
