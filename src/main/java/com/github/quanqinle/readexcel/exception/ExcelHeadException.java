package com.github.quanqinle.readexcel.exception;

/**
 * 表头异常
 * 如，用于表格格式与预期不符
 *
 * @author quanqinle
 */
public class ExcelHeadException extends RuntimeException {
    public ExcelHeadException(String message) {
        super(message);
    }
}
