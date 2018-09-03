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
import java.util.Date;

public class DateUtil {
	public static final DateTimeFormatter DF_ONLY_YMD_S1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter DF_ONLY_YMDHM_S1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat DF_ONLY_YMD_S1_OLD = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateTimeFormatter DF_ONLY_YMD_S2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	public static final DateTimeFormatter DF_FULL_S1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DF_FULL_S2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final SimpleDateFormat DF_ONLY_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	public static long subTime(Date date1,Date date2,int type){
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long time = time1-time2;
		long sub = 0l;
		if(type == 1){//间隔秒数
			sub = time/1000;
		}else if(type == 2){//间隔分数
			sub = time/(1000*60);
		}else if(type == 3){//间隔小时
			sub = time/(1000*60*60);
		}else if(type == 4){//间隔天数
			sub = time/(1000*60*60*24);
		}
		return sub;
	}

}
