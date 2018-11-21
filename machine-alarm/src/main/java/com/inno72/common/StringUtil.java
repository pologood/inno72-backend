package com.inno72.common;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import com.inno72.log.PointLogContext;
import com.inno72.log.vo.LogType;

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

	public static String setText(String text, String active) {
		if (StringUtil.isEmpty(active) || "dev".equals(active)) {
			text = "开发：" + text;
		} else if ("test".equals(active)) {
			text = "测试：" + text;
		} else if ("stage".equals(active)) {
			text = "预发：" + text;
		}
		return text;
	}

	public static Boolean senSmsActive(String active) {
		return true;
//		if (StringUtil.isNotEmpty(active) && (active.equals("prod") || active.equals("stage"))) {
//			return true;
//		} else {
//			return false;
//		}
	}

	/**
	 * @param msg
	 *            消息体 msg[0] type 日志类型 msg[1] machineCode 机器code msg[2] detail
	 *            详情
	 */
	public static void logger(String... msg) {
		new PointLogContext(LogType.POINT).machineCode(msg[1])
				.pointTime(DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S1)).type(msg[0]).detail(msg[2])
				.tag("").bulid();
	}

	public static int getAreaCodeNum(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				if (i < 2) {
					return 2;
				} else if ((i + 1) < 4) {
					return 3;
				} else {
					return i + 1;
				}
			}
		}
		return 0;
	}

}
