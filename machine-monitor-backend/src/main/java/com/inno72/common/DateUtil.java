/**   
 * @Title: DateUtil.java 
 * @Package com.myapp.security.util 
 * @Description: 
 * @author wulei   
 * @date 2011-8-31 下午08:39:37  
 */
package com.inno72.common;

import java.text.Format;
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
	public static final SimpleDateFormat DF_ONLY_ALL_S1_OLD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

	/**
	 * 比较两个时间相差多少小时(分钟、秒)
	 * @date Jan 2, 2014
	 * @param startTime 需要比较的时间 不能为空，且必须符合正确格式：2012-12-12 12:12:
	 * @param endTime 需要被比较的时间 若为空则默认当前时间
	 * @param type 1：小时   2：分钟   3：秒  4：天
	 * @return int
	 */
	public static int compareTime(Date startTime , Date endTime , int type) {

		int value = 0;
		try {
			long between = (endTime.getTime() - startTime.getTime());
			//小时
			if(type == 1){
				value = (int) (between /(60*60*1000));
			}//分钟
			else if(type == 2){
				value = (int) (between /(60*1000));
			}//秒
			else if(type == 3){
				value = (int) (between /1000);
			}
			else if(type == 4){
				value = (int) (between /(24*60*60*1000));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获取当前时间，格式为：yyyy-MM-dd HH:mm:ss
	 * @author chenssy
	 * @date Dec 27, 2013
	 * @return String
	 */
	public static String getCurrentTime(){
		return getCurrentTime(DateFormatUtils.DATE_FORMAT2);
	}

	/**
	 * 根据指定格式获取当前时间
	 * @author chenssy
	 * @date Dec 27, 2013
	 * @param format
	 * @return String
	 */
	public static String getCurrentTime(String format){
		SimpleDateFormat sdf = DateFormatUtils.getFormat(format);
		Date date = new Date();
		return sdf.format(date);
	}

	/*public static void main(String[] args) {


		Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date today = new Date();
		System.out.println("今天是:" + f.format(today));

		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DATE, 1);// 今天+1天

		Date tomorrow = c.getTime();
		System.out.println("明天是:" + f.format(tomorrow));

		int result = compareTime(today,tomorrow,4);

		System.out.println("调用发送结果是" + result);
	}*/




}
