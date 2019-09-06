package com.github.quanqinle.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间函数
 *
 * @author 权芹乐
 */
public class DateTimeUtil {

	// for debug
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			System.out.println(getCurrentDateTime(i));
		}
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return 当前日期时间，形如20130123150123
	 * @author 权芹乐 2013-1-23
	 */
	public static String getCurrentDateTime() {
		return DateTimeUtil.getCurrentDateTime(0);
	}

	/**
	 * 根据设置格式类型，获取当前日期时间
	 *
	 * @param type
	 *          返回日期时间形式<br>
	 *          0, 20130123150123<br>
	 *          1, 2013-01-23T15:01:23.357<br>
	 *          2, 20130123<br>
	 *          3, 150123<br>
	 *          4, 15:01:23.456<br>
	 *          5, 2013-01-23 15:01:23<br>
	 *          默认, 20130123150123<br>
	 * @return String 日期时间字符串
	 */
	public static String getCurrentDateTime(int type) {
		String pattern = "yyyyMMddHHmmss";

		switch (type) {
		case 0: // 默认值
			break;
		case 1:
			pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
			break;
		case 2:
			pattern = "yyyyMMdd";
			break;
		case 3:
			pattern = "HHmmss";
			break;
		case 4:
			pattern = "HH:mm:ss.SSS";
			break;
		case 5:
			pattern = "yyyy-MM-dd HH:mm:ss";
			break;
		default: // 默认值
			break;
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA); // 设置日期格式
		return df.format(new Date());
	}

	private static final String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	/**
	 * 根据指定格式获取当前时间
	 *
	 * @param format
	 * @return String
	 * @author chenssy
	 * @date Dec 27, 2013
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 * 获取当前时间，格式为：yyyy-MM-dd HH:mm:ss
	 *
	 * @return String
	 * @author chenssy
	 * @date Dec 27, 2013
	 */
	public static String getCurrentTime() {
		return getCurrentTime(DateFormatUtil.DATE_FORMAT2);
	}

	/**
	 * 获取指定格式的当前时间：为空时格式为yyyy-mm-dd HH:mm:ss
	 *
	 * @param format
	 * @return Date
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static Date getCurrentDate(String format) {
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
		String dateS = getCurrentTime(format);
		Date date = null;
		try {
			date = sdf.parse(dateS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @return Date
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static Date getCurrentDate() {
		return getCurrentDate(DateFormatUtil.DATE_FORMAT2);
	}

	/**
	 * 给指定日期加入年份，为空时默认当前时间
	 *
	 * @param year
	 *          年份 正数相加、负数相减
	 * @param date
	 *          为空时，默认为当前时间
	 * @param format
	 *          默认格式为：yyyy-MM-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static String addYearToDate(int year, Date date, String format) {
		Calendar calender = getCalendar(date, format);
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

		calender.add(Calendar.YEAR, year);

		return sdf.format(calender.getTime());
	}

	/**
	 * 给指定日期加入年份，为空时默认当前时间
	 *
	 * @param year
	 *          年份 正数相加、负数相减
	 * @param date
	 *          为空时，默认为当前时间
	 * @param format
	 *          默认格式为：yyyy-MM-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static String addYearToDate(int year, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addYearToDate(year, newDate, format);
	}

	/**
	 * 给指定日期增加月份 为空时默认当前时间
	 *
	 * @param month
	 *          增加月份 正数相加、负数相减
	 * @param date
	 *          指定时间
	 * @param format
	 *          指定格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static String addMothToDate(int month, Date date, String format) {
		Calendar calender = getCalendar(date, format);
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

		calender.add(Calendar.MONTH, month);

		return sdf.format(calender.getTime());
	}

	/**
	 * 给指定日期增加月份 为空时默认当前时间
	 *
	 * @param month
	 *          增加月份 正数相加、负数相减
	 * @param date
	 *          指定时间
	 * @param format
	 *          指定格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static String addMothToDate(int month, String date, String format) {
		Date newDate = new Date();
		if (null != date && !"".equals(date)) {
			newDate = string2Date(date, format);
		}

		return addMothToDate(month, newDate, format);
	}

	/**
	 * 给指定日期增加天数，为空时默认当前时间
	 *
	 * @param day
	 *          增加天数 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addDayToDate(int day, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

		calendar.add(Calendar.DATE, day);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定日期增加天数，为空时默认当前时间
	 *
	 * @param day
	 *          增加天数 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addDayToDate(int day, String date, String format) {
		Date newDate = new Date();
		if (!StringUtils.isBlank(date)) {
			newDate = string2Date(date, format);
		}

		return addDayToDate(day, newDate, format);
	}

	/**
	 * 给指定日期增加小时，为空时默认当前时间
	 *
	 * @param hour
	 *          增加小时 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addHourToDate(int hour, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

		calendar.add(Calendar.HOUR, hour);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定日期增加小时，为空时默认当前时间
	 *
	 * @param hour
	 *          增加小时 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addHourToDate(int hour, String date, String format) {
		Date newDate = new Date();
		if (!StringUtils.isBlank(date)) {
			newDate = string2Date(date, format);
		}

		return addHourToDate(hour, newDate, format);
	}

	/**
	 * 给指定的日期增加分钟，为空时默认当前时间
	 *
	 * @param minute
	 *          增加分钟 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addMinuteToDate(int minute, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

		calendar.add(Calendar.MINUTE, minute);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定的日期增加分钟，为空时默认当前时间
	 *
	 * @param minute
	 *          增加分钟 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addMinuteToDate(int minute, String date, String format) {
		Date newDate = new Date();
		if (!StringUtils.isBlank(date)) {
			newDate = string2Date(date, format);
		}

		return addMinuteToDate(minute, newDate, format);
	}

	/**
	 * 给指定日期增加秒，为空时默认当前时间
	 *
	 * @param second
	 *          增加秒 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addSecondToDate(int second, Date date, String format) {
		Calendar calendar = getCalendar(date, format);
		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);

		calendar.add(Calendar.SECOND, second);

		return sdf.format(calendar.getTime());
	}

	/**
	 * 给指定日期增加秒，为空时默认当前时间
	 *
	 * @param second
	 *          增加秒 正数相加、负数相减
	 * @param date
	 *          指定日期
	 * @param format
	 *          日期格式 为空默认 yyyy-mm-dd HH:mm:ss
	 * @return String
	 * @throws Exception
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String addSecondToDate(int second, String date, String format) {
		Date newDate = new Date();
		if (!StringUtils.isBlank(date)) {
			newDate = string2Date(date, format);
		}

		return addSecondToDate(second, newDate, format);
	}

	/**
	 * 获取指定格式指定时间的日历
	 *
	 * @param date
	 *          时间
	 * @param format
	 *          格式
	 * @return Calendar
	 * @author chenssy
	 * @date Dec 30, 2013
	 */
	public static Calendar getCalendar(Date date, String format) {
		if (date == null) {
			date = getCurrentDate(format);
		}

		Calendar calender = Calendar.getInstance();
		calender.setTime(date);

		return calender;
	}

	/**
	 * 字符串转换为日期，日期格式为
	 *
	 * @param value
	 * @return
	 * @author : chenssy
	 * @date : 2016年5月31日 下午5:20:22
	 */
	public static Date string2Date(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		SimpleDateFormat sdf = DateFormatUtil.getFormat(DateFormatUtil.DATE_FORMAT2);
		Date date = null;

		try {
			value = DateFormatUtil.formatDate(value, DateFormatUtil.DATE_FORMAT2);
			date = sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将字符串(格式符合规范)转换成Date
	 *
	 * @param value
	 *          需要转换的字符串
	 * @param format
	 *          日期格式
	 * @return Date
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static Date string2Date(String value, String format) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
		Date date = null;

		try {
			value = DateFormatUtil.formatDate(value, format);
			date = sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将日期格式转换成String
	 *
	 * @param value
	 *          需要转换的日期
	 * @param format
	 *          日期格式
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String date2String(Date value, String format) {
		if (value == null) {
			return null;
		}

		SimpleDateFormat sdf = DateFormatUtil.getFormat(format);
		return sdf.format(value);
	}

	/**
	 * 日期转换为字符串
	 *
	 * @param value
	 * @return
	 * @author : chenssy
	 * @date : 2016年5月31日 下午5:21:38
	 */
	public static String date2String(Date value) {
		if (value == null) {
			return null;
		}

		SimpleDateFormat sdf = DateFormatUtil.getFormat(DateFormatUtil.DATE_FORMAT2);
		return sdf.format(value);
	}

	/**
	 * 获取指定日期的年份
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentYear(Date value) {
		String date = date2String(value, DateFormatUtil.DATE_YEAR);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的年份
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentYear(String value) {
		Date date = string2Date(value, DateFormatUtil.DATE_YEAR);
		Calendar calendar = getCalendar(date, DateFormatUtil.DATE_YEAR);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取指定日期的月份
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentMonth(Date value) {
		String date = date2String(value, DateFormatUtil.DATE_MONTH);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的月份
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentMonth(String value) {
		Date date = string2Date(value, DateFormatUtil.DATE_MONTH);
		Calendar calendar = getCalendar(date, DateFormatUtil.DATE_MONTH);

		return calendar.get(Calendar.MONTH);
	}

	/**
	 * 获取指定日期的天份
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentDay(Date value) {
		String date = date2String(value, DateFormatUtil.DATE_DAY);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的天份
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentDay(String value) {
		Date date = string2Date(value, DateFormatUtil.DATE_DAY);
		Calendar calendar = getCalendar(date, DateFormatUtil.DATE_DAY);

		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取当前日期为星期几
	 *
	 * @param value
	 *          日期
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String getCurrentWeek(Date value) {
		Calendar calendar = getCalendar(value, DateFormatUtil.DATE_FORMAT1);
		int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 0 : calendar.get(Calendar.DAY_OF_WEEK) - 1;

		return weeks[weekIndex];
	}

	/**
	 * 获取当前日期为星期几
	 *
	 * @param value
	 *          日期
	 * @return String
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static String getCurrentWeek(String value) {
		Date date = string2Date(value, DateFormatUtil.DATE_FORMAT1);
		return getCurrentWeek(date);
	}

	/**
	 * 获取指定日期的小时
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentHour(Date value) {
		String date = date2String(value, DateFormatUtil.DATE_HOUR);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的小时
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentHour(String value) {
		Date date = string2Date(value, DateFormatUtil.DATE_HOUR);
		Calendar calendar = getCalendar(date, DateFormatUtil.DATE_HOUR);

		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取指定日期的分钟
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentMinute(Date value) {
		String date = date2String(value, DateFormatUtil.DATE_MINUTE);
		return Integer.valueOf(date);
	}

	/**
	 * 获取指定日期的分钟
	 *
	 * @param value
	 *          日期
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int getCurrentMinute(String value) {
		Date date = string2Date(value, DateFormatUtil.DATE_MINUTE);
		Calendar calendar = getCalendar(date, DateFormatUtil.DATE_MINUTE);

		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 比较两个日期相隔多少天(月、年) <br>
	 * 例：<br>
	 * &nbsp;compareDate("2009-09-12", null, 0);//比较天 <br>
	 * &nbsp;compareDate("2009-09-12", null, 1);//比较月 <br>
	 * &nbsp;compareDate("2009-09-12", null, 2);//比较年 <br>
	 *
	 * @param startDay
	 *          需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
	 * @param endDay
	 *          被比较的时间 为空(null)则为当前时间
	 * @param type
	 *          返回值类型 0为多少天，1为多少个月，2为多少年
	 * @return int
	 * @author chenssy
	 * @date Dec 31, 2013
	 */
	public static int compareDate(String startDay, String endDay, int type) {
		int n = 0;
		startDay = DateFormatUtil.formatDate(startDay, "yyyy-MM-dd");
		endDay = DateFormatUtil.formatDate(endDay, "yyyy-MM-dd");

		String formatStyle = "yyyy-MM-dd";
		if (1 == type) {
			formatStyle = "yyyy-MM";
		} else if (2 == type) {
			formatStyle = "yyyy";
		}

		endDay = endDay == null ? getCurrentTime("yyyy-MM-dd") : endDay;

		DateFormat df = new SimpleDateFormat(formatStyle);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(startDay));
			c2.setTime(df.parse(endDay));
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			n++;
			if (type == 1) {
				c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
			} else {
				c1.add(Calendar.DATE, 1); // 比较天数，日期+1
			}
		}
		n = n - 1;
		if (type == 2) {
			n = (int) n / 365;
		}
		return n;
	}

	/**
	 * 比较两个时间相差多少小时(分钟、秒)
	 *
	 * @param startTime
	 *          需要比较的时间 不能为空，且必须符合正确格式：2012-12-12 12:12:
	 * @param endTime
	 *          需要被比较的时间 若为空则默认当前时间
	 * @param type
	 *          1：小时 2：分钟 3：秒
	 * @return int
	 * @author chenssy
	 * @date Jan 2, 2014
	 */
	public static int compareTime(String startTime, String endTime, int type) {
		// endTime是否为空，为空默认当前时间
		if (endTime == null || "".equals(endTime)) {
			endTime = getCurrentTime();
		}

		SimpleDateFormat sdf = DateFormatUtil.getFormat("");
		int value = 0;
		try {
			Date begin = sdf.parse(startTime);
			Date end = sdf.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000; // 除以1000转换成豪秒
			if (type == 1) { // 小时
				value = (int) (between % (24 * 36000) / 3600);
			} else if (type == 2) {
				value = (int) (between % 3600 / 60);
			} else if (type == 3) {
				value = (int) (between % 60 / 60);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 比较两个日期的大小。<br>
	 * 若date1 > date2 则返回 1<br>
	 * 若date1 = date2 则返回 0<br>
	 * 若date1 < date2 则返回-1
	 *
	 * @param date1
	 * @param date2
	 * @param format
	 *          待转换的格式
	 * @return 比较结果
	 * @autor:chenssy
	 * @date:2014年9月9日
	 */
	public static int compare(String date1, String date2, String format) {
		DateFormat df = DateFormatUtil.getFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取指定月份的第一天
	 *
	 * @param date
	 * @return
	 * @author : chenssy
	 * @date : 2016年5月31日 下午5:31:10
	 */
	public static String getMonthFirstDate(String date) {
		date = DateFormatUtil.formatDate(date);
		return DateFormatUtil.formatDate(date, "yyyy-MM") + "-01";
	}

	/**
	 * 获取指定月份的最后一天
	 *
	 * @param strdate
	 * @return
	 * @author : chenssy
	 * @date : 2016年5月31日 下午5:32:09
	 */
	public static String getMonthLastDate(String date) {
		Date strDate = string2Date(getMonthFirstDate(date));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return DateFormatUtil.formatDate(calendar.getTime());
	}

	/**
	 * 获取所在星期的第一天
	 *
	 * @param date
	 * @return
	 * @author : chenssy
	 * @date : 2016年6月1日 下午12:38:53
	 */
	@SuppressWarnings("static-access")
	public static Date getWeekFirstDate(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int today = now.get(Calendar.DAY_OF_WEEK);
		int first_day_of_week = now.get(Calendar.DATE) + 2 - today; // 星期一
		now.set(now.DATE, first_day_of_week);
		return now.getTime();
	}

	/**
	 * 获取所在星期的最后一天
	 *
	 * @param date
	 * @return
	 * @author : chenssy
	 * @date : 2016年6月1日 下午12:40:31
	 */
	@SuppressWarnings("static-access")
	public static Date geWeektLastDate(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		int today = now.get(Calendar.DAY_OF_WEEK);
		int first_day_of_week = now.get(Calendar.DATE) + 2 - today; // 星期一
		int last_day_of_week = first_day_of_week + 6; // 星期日
		now.set(now.DATE, last_day_of_week);
		return now.getTime();
	}
}
