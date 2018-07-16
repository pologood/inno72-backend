package com.inno72.common;

import java.util.UUID;

public class StringUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
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
