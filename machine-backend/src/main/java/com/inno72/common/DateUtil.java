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
import java.util.Calendar;
import java.util.Date;

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
	
	public static LocalDateTime toDateTime(String str, DateTimeFormatter dateTimeFormatter) {
		try {
			return LocalDateTime.parse(str, dateTimeFormatter);
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
	
	/**
     * 在原日期的基础上增加天数
     * @param date
     * @param i
     * @return
     */
    public static Date addDayOfDate(Date date,int i){
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, i);
	    Date newDate = c.getTime();
	    
	    return newDate;
    }

}
