package com.github.quanqinle.util.excelutil.parser.impl;

import com.github.quanqinle.util.LogUtil;
import com.github.quanqinle.util.excelutil.handler.ExcelParserHandler;
import com.github.quanqinle.util.excelutil.handler.impl.Excel2003ParserHandler;
import com.github.quanqinle.util.excelutil.handler.impl.Excel2007ParserHandler;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.util.IOUtils;

import java.io.InputStream;

public class ExcelSaxParser<T> extends AbstractExcelParser<T> {

	public ExcelParserHandler<T> createHandler(InputStream excelInputStream) {
		try {
			byte[] header8 = IOUtils.peekFirst8Bytes(excelInputStream);
			if (NPOIFSFileSystem.hasPOIFSHeader(header8)) {
				return new Excel2003ParserHandler<>();
			} else if (DocumentFactoryHelper.hasOOXMLHeader(excelInputStream)) {
				return new Excel2007ParserHandler<>();
			} else {
				throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
			}
		} catch (Exception e) {
			LogUtil.error("getParserInstance Error!" + e);
			throw new RuntimeException(e);
		}
	}

}
