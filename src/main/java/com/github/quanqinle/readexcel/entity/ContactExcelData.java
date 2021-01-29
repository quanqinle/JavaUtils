package com.github.quanqinle.readexcel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.github.quanqinle.readexcel.converter.DealTypeConverter;
import com.github.quanqinle.readexcel.converter.SampleFeatureConverter;
import com.github.quanqinle.readexcel.converter.SendTypeConverter;
import com.github.quanqinle.readexcel.converter.SubjectConverter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 注意：默认第一个字段就是index=0
 * @ExcelProperty中，value和index只能用1个，优先value
 *
 * @author quanqinle
 */
@Data
public class ContactExcelData {

    /**
     * 表头所在行
     * 行号从1开始
     */
    @ExcelIgnore
    public final static int HEAD_ROW_NUMBER = 5;
    /**
     * 列数
     * 注意：index从0开始，last_index==列数-1
     */
    @ExcelIgnore
    public final static int COLUMN_LAST_NUMBER = 37;

    /**
     * 无法通过列名value定位的列，统一将index维护在这里。如，存在重名的列名
     */
    @ExcelIgnore
    private final static int REMARK1_INDEX = 20;
    @ExcelIgnore
    private final static int REMARK2_INDEX = 28;
    @ExcelIgnore
    private final static int PARAM1_INDEX = COLUMN_LAST_NUMBER -8;
    @ExcelIgnore
    private final static int PARAM2_INDEX = COLUMN_LAST_NUMBER -7;
    @ExcelIgnore
    private final static int PARAM3_INDEX = COLUMN_LAST_NUMBER -6;
    @ExcelIgnore
    private final static int PARAM4_INDEX = COLUMN_LAST_NUMBER -5;
    @ExcelIgnore
    private final static int PARAM5_INDEX = COLUMN_LAST_NUMBER -4;
    @ExcelIgnore
    private final static int PARAM6_INDEX = COLUMN_LAST_NUMBER -3;
    @ExcelIgnore
    private final static int PARAM7_INDEX = COLUMN_LAST_NUMBER -2;
    @ExcelIgnore
    private final static int  OTHER_INDEX = COLUMN_LAST_NUMBER -1;


    /**
     * 期望的表头。
     * 主要用于表格合法性校验。这里可以只校验必要的字段，即，配置实际excel的表头字段的子集。
     * key是表头排序，从0开始；
     * value是表头名，可以忽略前后空格，但必须包含中间空格和换行
     */
    @ExcelIgnore
    public final static Map<Integer, String> HEAD_CHECK_MAP = new HashMap<>() {
        {
            put( 0, "索引号");
            put( 1, "选取样本特征");
            put( 2, "发函单位（客户）*");
            put( 3, "发函方式");
            put( 4, "发函快递公司");
            put( 5, "被函单位*");
            put( 6, "省");
            put( 7, "市");
            put( 8, "行政区");
            put( 9, "详细地址");
            put(10, "被函单位邮箱");
            put(11, "被函单位收件人");
            put(12, "收件人电话");
            put(13, "会计科目");
            put(14, "截止日期");
            put(15, "单位");
            put(16, "贵公司欠本公司");
            put(17, "本公司欠贵公司");
            put(18, "其中：已开票金额");
            put(19, "其中：未开票金额");
            put(20, "备注");
            put(21, "事项");
            put(22, "交易类型");
            put(23, "期间");
            put(24, "数量");
            put(25, "单位");
            put(26, "金额（含税）");
            put(27, "金额（不含税）");
            put(28, "备注");
        }
    };


    /* ---- 以下是表格一一对应的字段 ---- */

    /**
     * 函证基本信息
     */
    @ExcelProperty({"索引号"})
    private String keyId;
    @ExcelProperty(value = "选取样本特征", converter = SampleFeatureConverter.class)
    private String sampleFeature;
    @ExcelProperty("发函单位（客户）*")
    private String sendCompany;
    @ExcelProperty(value = "发函方式", converter = SendTypeConverter.class)
    private String sendType;
    @ExcelProperty("发函快递公司")
    private String defualtSendExpress;
    @ExcelProperty("被函单位*")
    private String company;
    @ExcelProperty("省")
    private String companyProvince;
    @ExcelProperty("市")
    private String companyCity;
    @ExcelProperty("行政区")
    private String companyArea;
    @ExcelProperty("详细地址")
    private String companyAddress;
    @ExcelProperty("被函单位邮箱")
    private String companyEmail;
    @ExcelProperty("被函单位收件人")
    private String companyUser;
    @ExcelProperty("收件人电话")
    private String companyMobile;

    /**
     * 1、本公司与贵公司的往来账项列示如下：
     */
    @ExcelProperty(value = "会计科目", converter = SubjectConverter.class)
    private String subject;
    @ExcelProperty("截止日期")
    @DateTimeFormat("yyyy-MM-dd")
    private String endTime;
    @ExcelProperty("单位")
    private String unit1;
    @ExcelProperty("贵公司欠本公司")
    @NumberFormat("#.##")
    private String owes;
    @ExcelProperty("本公司欠贵公司")
    @NumberFormat("#.##%")
    private String owed;
    @ExcelProperty("其中：已开票金额")
    @NumberFormat("#.##")
    private String invoiceAccrual;
    @ExcelProperty("其中：未开票金额")
    @NumberFormat("#.##")
    private String noInvoiceAccrual;
    /**
     * 备注。注意：有重名字段
     */
    @ExcelProperty(index = REMARK1_INDEX)
    private String remark1;

    /**
     * 2、本公司与贵公司交易列示如下：
     */
    @ExcelProperty("事项")
    private String item;
    @ExcelProperty(value = "交易类型", converter = DealTypeConverter.class)
    private String dealType;
    @ExcelProperty("期间")
    private String section;
    @ExcelProperty("数量")
    @NumberFormat("#.##")
    private String quantity;
    @ExcelProperty("单位")
    private String unit2;
    @ExcelProperty("金额（含税）")
    @NumberFormat("#.##")
    private String amountIncludingTax;
    @ExcelProperty("金额（不含税）")
    @NumberFormat("#.##")
    private String amountExcludingTax;
    /**
     * 备注。注意：有重名字段
     */
    @ExcelProperty(index = REMARK2_INDEX)
    private String remark2;

    /**
     * 3、本公司与贵公司重要事项列示如下：
     */
    @ExcelProperty(index = PARAM1_INDEX)
    private String param1;
    @ExcelProperty(index = PARAM2_INDEX)
    private String param2;
    @ExcelProperty(index = PARAM3_INDEX)
    private String param3;
    @ExcelProperty(index = PARAM4_INDEX)
    private String param4;
    @ExcelProperty(index = PARAM5_INDEX)
    private String param5;
    @ExcelProperty(index = PARAM6_INDEX)
    private String param6;
    @ExcelProperty(index = PARAM7_INDEX)
    private String param7;

    /**
     * 4、其他事项
     */
    @ExcelProperty(index = OTHER_INDEX)
    private String other;
}
