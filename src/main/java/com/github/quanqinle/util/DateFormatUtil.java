package com.github.quanqinle.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期格式化工具类
 *
 * @Author: quanqinle
 */
public class DateFormatUtil {
    /**
     * yyyy:年
     */
    public static final String DATE_YEAR = "yyyy";

    /**
     * MM：月
     */
    public static final String DATE_MONTH = "MM";

    /**
     * DD：日
     */
    public static final String DATE_DAY = "dd";

    /**
     * HH：时
     */
    public static final String DATE_HOUR = "HH";

    /**
     * mm：分
     */
    public static final String DATE_MINUTE = "mm";

    /**
     * ss：秒
     */
    public static final String DATE_SECONDES = "ss";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT1 = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static final String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd hh:mm:ss|SSS
     */
    public static final String TIME_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss|SSS";

    /**
     * yyyyMMdd
     */
    public static final String DATE_NOFUll_FORMAT = "yyyyMMdd";

    /**
     * yyyyMMddhhmmss
     */
    public static final String TIME_NOFUll_FORMAT = "yyyyMMddHHmmss";

    /**
     * 格式转换<br>
     * yyyy-MM-dd hh:mm:ss 和 yyyyMMddhhmmss 相互转换<br>
     * yyyy-mm-dd 和 yyyymmss 相互转换
     *
     * @param value
     *            日期
     * @return String
     */
    public static String formatString(String value) {
        String sReturn = "";
        if (StringUtils.isBlank(value)) {
            return sReturn;
        }
        /*
         * yyyymmddhhmmss 格式转换成 yyyy-mm-dd hh:mm:ss
         */
        if (value.length() == 14) {
            sReturn = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " "
                    + value.substring(8, 10) + ":" + value.substring(10, 12) + ":" + value.substring(12, 14);
        }
        /*
         * yyyy-mm-dd hh:mm:ss 格式转换成 yyyymmddhhmmss
         */
        else if (value.length() == 19) {
            sReturn.replace("-", "").replace(":", "").replace(" ", "");
        }
        /*
         * yyyy-mm-dd 格式转换成 yyyymmhh
         */
        else if (value.length() == 10) {
            sReturn.replace("-", "");
        }
        /*
         * yyyy-mm-dd 格式转换成 yyyymmhh
         */
        else if (value.length() == 8) {
            sReturn = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
        }
        return sReturn;
    }

    /**
     * 将日期字符串解析成指定的格式
     * 
     * @param date
     * @param format
     * @return 如解析失败，则直接返回date
     */
    public static String formatDate(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return "";
        }
        date = date.replace("-", "").replace(":", "").trim();
        if (StringUtils.isBlank(date)) {
            return "";
        }

        Date dt = null;
        SimpleDateFormat inFmt = null;
        SimpleDateFormat outFmt = null;
        ParsePosition pos = new ParsePosition(0);

        try {
            switch (date.length()) {
            case 14:
                inFmt = new SimpleDateFormat("yyyyMMddHHmmss");
                break;
            case 12:
                inFmt = new SimpleDateFormat("yyyyMMddHHmm");
                break;
            case 10:
                inFmt = new SimpleDateFormat("yyyyMMddHH");
                break;
            case 8:
                inFmt = new SimpleDateFormat("yyyyMMdd");
                break;
            case 6:
                inFmt = new SimpleDateFormat("yyyyMM");
                break;
            case 7:
            case 9:
            case 11:
            case 13:
            default:
                return date;
            }

            if ((dt = inFmt.parse(date, pos)) == null) {
                return date;
            }
            if (StringUtils.isBlank(format)) {
                outFmt = new SimpleDateFormat("yyyy年MM月dd日");
            } else {
                outFmt = new SimpleDateFormat(format);
            }
            return outFmt.format(dt);
        } catch (Exception ex) {
        }
        return date;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     * @author chenming
     * @date 2016年5月31日
     */
    public static String formatDate(Date date, String format) {
        return formatDate(DateTimeUtil.date2String(date), format);
    }

    /**
     * @param value
     * @return
     * @desc:格式化是时间，采用默认格式（yyyy-MM-dd HH:mm:ss）
     * @autor:chenssy
     * @date:2014年8月6日
     */
    public static String formatDate(String value) {
        return getFormat(DATE_FORMAT2).format(DateTimeUtil.string2Date(value, DATE_FORMAT2));
    }

    /**
     * 格式化日期
     *
     * @param value
     * @return
     * @author : chenssy
     * @date : 2016年5月31日 下午5:40:58
     */
    public static String formatDate(Date value) {
        return formatDate(DateTimeUtil.date2String(value));
    }

    /**
     * 构建日期显示格式。入参为空，则默认为yyyy-mm-dd HH:mm:ss
     *
     * @param format
     * @return SimpleDateFormat
     */
    protected static SimpleDateFormat getFormat(String format) {
        if (StringUtils.isBlank(format)) {
            format = DATE_FORMAT2;
        }
        return new SimpleDateFormat(format);
    }

    public static void main(String[] args) {
        LogUtil.info("start:" + DateTimeUtil.getCurrentDateTime());

        String s = null;
        // LogUtil.info(s.replace("-", "")); // fail

        s = "";
        LogUtil.info(s.replace("-", "")); // ok
        LogUtil.info("end  :" + DateTimeUtil.getCurrentDateTime());
    }
}
