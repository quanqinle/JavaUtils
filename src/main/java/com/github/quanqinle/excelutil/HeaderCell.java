package com.github.quanqinle.excelutil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

/**
 * @author quanql
 *
 */
public class HeaderCell implements Comparable<HeaderCell> {

	/**
	 * column or row index of this cell
	 */
	private int index = -1;
	/**
	 * 唯一性名称
	 */
	private String name = null;
	/**
	 * 别名分隔符
	 */
	private String separator = "||";
	/**
	 * 所有等同的名称（多个时，用||分隔）
	 */
	private List<String> aliasName = null;
	/**
	 * 前一个表头的唯一性名称
	 */
	private String preCellName = null;

	/**
	 * 
	 */
	public HeaderCell() {
	}

	/**
	 * @param name
	 * @param aliasName
	 * @param preCellName
	 */
	public HeaderCell(String name, String aliasName, String preCellName) {
		this(-1, name, null, aliasName, preCellName);
	}

	/**
	 * @param name
	 * @param separator
	 * @param aliasName
	 * @param preCellName
	 */
	public HeaderCell(String name, String separator, String aliasName, String preCellName) {
		this(-1, name, separator, aliasName, preCellName);
	}

	/**
	 * @param index
	 * @param name
	 * @param separator
	 * @param aliasName
	 * @param preCellName
	 */
	public HeaderCell(int index, String name, String separator, String aliasName, String preCellName) {
		this.index = index;
		this.name = delAllWhitespace(name);

		this.setSeparator(separator);
		this.setAliasName(aliasName);
		this.setPreCellName(preCellName);
	}

	/**
	 * @param index
	 * @param name
	 * @param aliasName
	 * @param preCellName
	 */
	public HeaderCell(int index, String name, List<String> aliasName, String preCellName) {
		this.index = index;
		this.name = delAllWhitespace(name);
		this.aliasName = aliasName;
		this.preCellName = delAllWhitespace(preCellName);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = delAllWhitespace(name);
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator
	 *            the separator to set
	 */
	public void setSeparator(String separator) {
		if (!StringUtils.isEmpty(separator)) {
			this.separator = delAllWhitespace(separator);
		}
	}

	/**
	 * @return the aliasName
	 */
	public List<String> getAliasName() {
		return aliasName;
	}

	/**
	 * @param aliasName
	 *            the aliasName to set
	 */
	public void setAliasName(String aliasName) {
		this.aliasName = Arrays.asList(StringUtils.split(delAllWhitespace(aliasName), separator));
	}

	/**
	 * @param aliasName
	 *            the aliasName to set
	 */
	public void setAliasName(List<String> aliasName) {
		this.aliasName = aliasName;
	}

	/**
	 * @return the preCellName
	 */
	public String getPreCellName() {
		return preCellName;
	}

	/**
	 * @param preCellName
	 *            the preCellName to set
	 */
	public void setPreCellName(String preCellName) {
		this.preCellName = delAllWhitespace(preCellName);
	}

	public String delAllWhitespace(String str) {
		return StringUtils.deleteWhitespace(str);
	}

	/**
	 * 判断name是否当前Cell的表头名
	 * 
	 * @param name
	 * @return
	 */
	public boolean isMe(String name) {
		String str = delAllWhitespace(name);
		if (StringUtils.equals(str, this.name)) {
			return true;
		}
		for (String alias : aliasName) {
			if (StringUtils.equals(alias, str)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HeaderCell c) {
		if (this.index < c.index) {
			return -1;
		} else if (this.index == c.index) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
