package com.github.quanqinle.util.excelutil;

import com.github.quanqinle.util.excelutil.param.ParamParser;
import com.github.quanqinle.util.excelutil.param.imp.DefaultParamParser;
import com.github.quanqinle.util.excelutil.parser.ExcelParser;
import com.github.quanqinle.util.excelutil.parser.impl.ExcelDomParser;
import com.github.quanqinle.util.excelutil.parser.impl.ExcelSaxParser;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class ParserTest {

    private ExcelParser<User> parser;

    @Test
    public void testDomXlsx() {

        parser = new ExcelDomParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xlsx"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET).targetClass(User.class).header(User.getHeader())
                .build();

        List<User> user = parser.parse(paramParser);
        System.out.println(user);
    }

    @Test
    public void testDomXls() {

        parser = new ExcelDomParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xls"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET).targetClass(User.class).header(User.getHeader())
                .build();

        List<User> user = parser.parse(paramParser);
        System.out.println(user);
    }

    @Test
    public void testSaxXlsx() {
        parser = new ExcelSaxParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xlsx"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET).targetClass(User.class).header(User.getHeader())
                .build();

        List<User> user = parser.parse(paramParser);
        System.out.println(user);
    }

    @Test
    public void testSaxXls() {
        parser = new ExcelSaxParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test01.xls"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET).targetClass(User.class).header(User.getHeader())
                .build();

        List<User> user = parser.parse(paramParser);
        System.out.println(user);
    }

    @Test
    public void testSheet02Xls() {
        parser = new ExcelSaxParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xls"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET + 1).targetClass(User.class).header(User.getHeader())
                .build();

        List<User> user = parser.parse(paramParser);
        System.out.println(user);
    }

    @Test
    public void testSheet02Xlsx() {
        parser = new ExcelSaxParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xlsx"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET + 1).targetClass(User.class).header(User.getHeader())
                .build();

        List<User> user = parser.parse(paramParser);
        System.out.println(user);
    }

    @Test
    public void testCheckHeaderErrorXlsx() {
        parser = new ExcelSaxParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xlsx"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET + 1).targetClass(User.class).header(User.getErrHeader())
                .build();
        try {
            List<User> user = parser.parse(paramParser);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        fail();
    }

    @Test
    public void testCheckHeaderErrorXls() {
        parser = new ExcelSaxParser<>();

        ParamParser paramParser = DefaultParamParser.builder()
                .excelInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test02.xls"))
                .columnSize(4).sheetNum(ParamParser.FIRST_SHEET + 1).targetClass(User.class).header(User.getErrHeader())
                .build();
        try {
            List<User> user = parser.parse(paramParser);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        fail();
    }

}
