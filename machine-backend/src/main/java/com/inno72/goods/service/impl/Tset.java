package com.inno72.goods.service.impl;

import org.apache.commons.lang3.StringUtils;

public class Tset {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String areCode = "100101000";//一共9位 省前2位后补0  市4位后补0 县6位后补0 商圈直接取
			//String s =fanzhuan(areCode);
			System.out.println(fanzhuan(areCode));
			
			String s = areCode.substring(0, 5+1);
			System.out.println(s);
			
	}
	
	
	public static int fanzhuan(String s){
		for (int i = s.length()-1; i >=0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				return i;
			}
		}
		return 0;
	}
}
