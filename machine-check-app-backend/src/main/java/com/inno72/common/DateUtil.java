/**   
 * @Title: DateUtil.java 
 * @Package com.myapp.security.util 
 * @Description: 
 * @author wulei   
 * @date 2011-8-31 下午08:39:37  
 */
package com.inno72.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	public static final DateTimeFormatter DF_ONLY_YMD_S1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter DF_ONLY_YMDHM_S1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat DF_ONLY_YMD_S1_OLD = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateTimeFormatter DF_ONLY_YMD_S2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	public static final DateTimeFormatter DF_FULL_S1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DF_FULL_S2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static LocalDate toDate(String str, DateTimeFormatter dateTimeFormatter) {
		try {
			return LocalDate.parse(str, dateTimeFormatter);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date toDateOld(String str, SimpleDateFormat simpleDateFormat) {
		try {
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String toTimeStr(LocalDateTime date, DateTimeFormatter dateTimeFormatter) {
		return dateTimeFormatter.format(date);
	}

	public static String toStr(LocalDate date, DateTimeFormatter dateTimeFormatter) {
		return dateTimeFormatter.format(date);
	}

	public static String toStrOld(Date date, SimpleDateFormat simpleDateFormat) {
		return simpleDateFormat.format(date);
	}

	public static String nowStr(){
		return toTimeStr(LocalDateTime.now(),DF_FULL_S1);
	}


	public static List<String> getDayListOfMonth() {
		List list = new ArrayList();
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int year = aCalendar.get(Calendar.YEAR);//年份
		int month = aCalendar.get(Calendar.MONTH) + 1;//月份
		int day = aCalendar.getActualMaximum(Calendar.DATE);
		for (int i = 1; i <= day; i++) {
			String aDate = String.valueOf(year)+"/"+month+"/"+i;
			list.add(aDate);
		}
		return list;
	}


	/**
	 * 某一年某个月的每一天
	 */
	public static List<String> getMonthFullDay(LocalDate localDate){
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		List<String> fullDayList = new ArrayList<String>();
		int day = 1;
		Calendar cal = Calendar.getInstance();// 获得当前日期对象
		cal.clear();// 清除信息
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);// 1月从0开始
		cal.set(Calendar.DAY_OF_MONTH, day);// 设置为1号,当前日期既为本月第一天
		int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int j = 0; j <= (count-1);) {
			if(DF_ONLY_YMD_S1_OLD.format(cal.getTime()).equals(getLastDay(year, month)))
				break;
			cal.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
			j++;
			fullDayList.add(DF_ONLY_YMD_S1_OLD.format(cal.getTime()));
		}
		return fullDayList;
	}

	public static String getLastDay(int year,int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return DF_ONLY_YMD_S1_OLD.format(cal.getTime());
	}

}
