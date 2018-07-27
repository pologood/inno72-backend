package com.inno72.common;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import com.inno72.common.PinyinUtil.Type;

public class StringUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String getMachineCode() {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		year = year.substring(2, 4);
		StringBuffer sb = new StringBuffer();
		String timestamp = String.valueOf(Clock.systemUTC().millis());
		timestamp = timestamp.substring(timestamp.length() - 5, timestamp.length());
		sb.append("ZJ");
		sb.append(year);
		String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
		sb.append(month);
		String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : String.valueOf(now.getDayOfMonth());
		sb.append(day);
		sb.append(timestamp);
		int _random = 0;
		try {
			_random = new Random().nextInt(89);
			_random += 10;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append(_random);
		return sb.toString();
	}

	public static String getGoodsCode(String shopName) {
		String key = PinyinUtil.toFirstPinYin(shopName, Type.UPPERCASE);
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		year = year.substring(2, 4);
		StringBuffer sb = new StringBuffer();
		String timestamp = String.valueOf(Clock.systemUTC().millis());
		timestamp = timestamp.substring(timestamp.length() - 3, timestamp.length());
		sb.append(key);
		sb.append(year);
		String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
		sb.append(month);
		String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : String.valueOf(now.getDayOfMonth());
		sb.append(day);
		sb.append(timestamp);
		int _random = 0;
		try {
			_random = new Random().nextInt(89);
			_random += 10;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append(_random);
		return sb.toString();
	}
	
	public static int getAreaCodeNum(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				return i + 1;
			}
		}
		return 0;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(final String str) {
		return (str == null) || (str.length() == 0);
	}

	/**
	 * 判断是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(final String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断是否空白
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(final String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return true;
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否不是空白
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(final String str) {
		return !isBlank(str);
	}

}
