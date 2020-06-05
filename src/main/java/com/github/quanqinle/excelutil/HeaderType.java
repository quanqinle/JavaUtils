package com.github.quanqinle.excelutil;

public enum HeaderType {
    HeaderRow(0), HeaderColumn(1);

    private final int type;

    private HeaderType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
