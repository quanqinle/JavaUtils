package com.github.quanqinle.util.excelutil.handler.impl;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.github.quanqinle.util.excelutil.param.ParamParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Excel2007ParserHandler<T> extends BaseExcelParserHandler<T>
    implements XSSFSheetXMLHandler.SheetContentsHandler {

	private int currentRow = -1;
	private int currentCol = -1;
	private ParamParser paramParser;
	private List<T> result;
	private List<String> rowData;

	public List<T> process(ParamParser paramParser) throws Exception {
		this.paramParser = paramParser;
		result = new ArrayList<>();
		rowData = initRowList(paramParser.getColumnSize());
		OPCPackage xlsxPackage = OPCPackage.open(paramParser.getExcelInputStream());
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage, false);
		XSSFReader xssfReader = new XSSFReader(xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		int needParse = 0;
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			if (needParse++ == paramParser.getSheetNum()) {
				processSheet(styles, strings, this, stream);
			}
			stream.close();
		}
		return result;
	}

	private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings,
	    XSSFSheetXMLHandler.SheetContentsHandler sheetHandler, InputStream sheetInputStream)
	    throws IOException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try {
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
		}
	}

	@Override
	public void startRow(int rowNum) {
		currentRow = rowNum;
		currentCol = -1;
	}

	@Override
	public void endRow(int rowNum) {
		handleEndOfRow(paramParser, rowData, result);
		rowData = initRowList(paramParser.getColumnSize());
		currentCol = -1;
	}

	@Override
	public void cell(String cellReference, String formattedValue, XSSFComment comment) {

		if (cellReference == null) {
			cellReference = new CellAddress(currentRow, currentCol).formatAsString();
		}

		currentCol = (new CellReference(cellReference)).getCol();
		if (currentCol < paramParser.getColumnSize())
			rowData.set(currentCol, formattedValue.trim());
	}

	@Override
	public void headerFooter(String text, boolean isHeader, String tagName) {

	}
}
