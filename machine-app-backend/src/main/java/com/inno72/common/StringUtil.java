package com.inno72.common;

import java.time.Clock;
import java.util.Random;
import java.util.UUID;

public class StringUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String getMachineCode() {
		StringBuffer sb = new StringBuffer();
		sb.append("18");
		String timestamp = String.valueOf(Clock.systemUTC().millis());
		timestamp = timestamp.substring(timestamp.length() - 4, timestamp.length());
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

	/**
	 * 生成验证码
	 * 
	 * @param count
	 * @return
	 */
	public static String createVerificationCode(int count) {
		String[] verificationCodeArrary = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String verificationCode = "";
		Random random = new Random();
		// 此处是生成验证码的核心了，利用一定范围内的随机数做为验证码数组的下标，循环组成我们需要长度的验证码，做为页面输入验证、邮件、短信验证码验证都行
		for (int i = 0; i < count; i++) {
			verificationCode += verificationCodeArrary[random.nextInt(verificationCodeArrary.length)];
		}
		return verificationCode;
	}

}
