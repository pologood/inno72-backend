package com.inno72.common;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
		String a = toFirstPinYin("伊利", Type.UPPERCASE);
		System.out.println("刘亚壮的运行测试结果为====" + a);
	}

	public static enum Type {
		UPPERCASE, // 全部大写
		LOWERCASE, // 全部小写
		FIRSTUPPER // 首字母大写
	}

	public String toPinYin(String str) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYin(str, "", Type.UPPERCASE);
	}

	public String toPinYin(String str, String spera) throws BadHanyuPinyinOutputFormatCombination {
		return toPinYin(str, spera, Type.UPPERCASE);
	}

	public static String toPinYin(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		if (str == null || str.trim().length() == 0)
			return "";
		if (type == Type.UPPERCASE)
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		else
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		String py = "";
		String temp = "";
		String[] t;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c <= 128)
				py += c;
			else {
				t = PinyinHelper.toHanyuPinyinStringArray(c, format);
				if (t == null)
					py += c;
				else {
					temp = t[0];
					if (type == Type.FIRSTUPPER)
						temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
					py += temp + (i == str.length() - 1 ? "" : spera);
				}
			}
		}
		return py.trim();
	}

	public static String toFirstPinYin(String str, Type type) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		if (str == null || str.trim().length() == 0)
			return "";
		if (type == Type.UPPERCASE)
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		else
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		String py = "";
		String temp = "";
		String[] t;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c <= 128)
				py += c;
			else {
				try {
					t = PinyinHelper.toHanyuPinyinStringArray(c, format);
					if (t == null)
						py += c;
					else {
						temp = t[0];
						if (type == Type.FIRSTUPPER) {
							temp = t[0].toUpperCase().charAt(0) + "";
						} else {
							temp = t[0].charAt(0) + "";
						}
						py += temp;
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}

			}
		}
		return py.trim();
	}

}
