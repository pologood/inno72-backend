package com.inno72.machine.vo;

import com.alibaba.fastjson.JSON;
import com.inno72.machine.model.Inno72Locale;

public class Inno72LocaleVo extends Inno72Locale {

	/**
	 * 省ID
	 */
	private String province;

	/**
	 * 市ID
	 */
	private String city;

	/**
	 * 区/县ID
	 */
	private String district;

	private Object tags;

	/**
	 * 商圈ID
	 */
	private String circle;

	private String areaName;

	private int userNum;

	private String machineCode;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Object getTags() {
		if (null != tags) {
			return JSON.parseArray(tags.toString(), String.class);
		}
		return null;
	}

	public void setTags(Object tags) {
		this.tags = tags;
	}

}