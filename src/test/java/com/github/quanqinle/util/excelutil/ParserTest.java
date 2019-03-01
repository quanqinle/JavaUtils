package com.github.quanqinle.util.excelutil;

import com.github.quanqinle.util.excelutil.param.ParserParam;
import com.github.quanqinle.util.excelutil.param.imp.DefaultParserParam;
import com.github.quanqinle.util.excelutil.parser.ExcelParser;
import com.github.quanqinle.util.excelutil.parser.impl.ExcelDomParser;
import com.github.quanqinle.util.excelutil.parser.impl.ExcelSaxParser;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

public class ParserTest {

  private ExcelParser<User> parser;

  @Test
  public void testDomXlsx() {

    parser = new ExcelDomParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xlsx"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET).targetClass(User.class).header(User.getHeader()).build();

    List<User> user = parser.parse(parserParam);
    System.out.println(user);
  }

  @Test
  public void testDomXls() {

    parser = new ExcelDomParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xls"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET).targetClass(User.class).header(User.getHeader()).build();

    List<User> user = parser.parse(parserParam);
    System.out.println(user);
  }

  @Test
  public void testSaxXlsx() {
    parser = new ExcelSaxParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xlsx"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET).targetClass(User.class).header(User.getHeader()).build();

    List<User> user = parser.parse(parserParam);
    System.out.println(user);
  }

  @Test
  public void testSaxXls() {
    parser = new ExcelSaxParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xls"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET).targetClass(User.class).header(User.getHeader()).build();

    List<User> user = parser.parse(parserParam);
    System.out.println(user);
  }

  @Test
  public void testSheet02Xls() {
    parser = new ExcelSaxParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xls"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET + 1).targetClass(User.class).header(User.getHeader()).build();

    List<User> user = parser.parse(parserParam);
    System.out.println(user);
  }

  @Test
  public void testSheet02Xlsx() {
    parser = new ExcelSaxParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xlsx"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET + 1).targetClass(User.class).header(User.getHeader()).build();

    List<User> user = parser.parse(parserParam);
    System.out.println(user);
  }

  @Test
  public void testCheckHeaderErrorXlsx() {
    parser = new ExcelSaxParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xlsx"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET + 1).targetClass(User.class).header(User.getErrHeader())
        .build();
    try {
      List<User> user = parser.parse(parserParam);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    fail();
  }

  @Test
  public void testCheckHeaderErrorXls() {
    parser = new ExcelSaxParser<>();

    ParserParam parserParam = DefaultParserParam.builder()
        .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xls"))
        .columnSize(4).sheetNum(ParserParam.FIRST_SHEET + 1).targetClass(User.class).header(User.getErrHeader())
        .build();
    try {
      List<User> user = parser.parse(parserParam);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    fail();
  }

}
