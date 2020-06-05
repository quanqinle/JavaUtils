package com.github.quanqinle.util.excelutil.handler.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import com.github.quanqinle.util.excelutil.param.ParamParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ExcelDomParserHandler<T> extends BaseExcelParserHandler<T> {

    @Override
    public List<T> process(ParamParser paramParser) throws Exception {
        Workbook workbook = generateWorkBook(paramParser);
        Sheet sheet = workbook.getSheetAt(paramParser.getSheetNum());
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (paramParser.getHeader() != null && paramParser.getHeader().size() != 0) {
            checkHeader(rowIterator, paramParser);
        }
        return parseRowToTargetList(rowIterator, paramParser);
    }

    private void checkHeader(Iterator<Row> rowIterator, ParamParser paramParser) {
        while (true) {
            Row row = rowIterator.next();
            List<String> rowData = parseRowToList(row, paramParser.getColumnSize());
            boolean empty = isRowDataEmpty(rowData);
            if (!empty) {
                validHeader(paramParser, rowData);
                break;
            }
        }
    }

    private Workbook generateWorkBook(ParamParser paramParser) throws IOException, InvalidFormatException {
        return WorkbookFactory.create(paramParser.getExcelInputStream());
    }

    private List<T> parseRowToTargetList(Iterator<Row> rowIterator, ParamParser paramParser)
            throws InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>();
        for (; rowIterator.hasNext();) {
            Row row = rowIterator.next();
            List<String> rowData = parseRowToList(row, paramParser.getColumnSize());
            Optional<T> d = parseRowToTarget(paramParser, rowData);
            d.ifPresent(result::add);
        }
        return result;
    }

    private List<String> parseRowToList(Row row, int size) {
        List<String> dataRow = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (row.getCell(i) != null) {
                DataFormatter formatter = new DataFormatter();
                String formattedCellValue = formatter.formatCellValue(row.getCell(i));
                dataRow.add(formattedCellValue.trim());
            } else {
                dataRow.add("");
            }
        }
        return dataRow;
    }
}
