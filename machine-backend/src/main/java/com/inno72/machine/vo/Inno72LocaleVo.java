package com.inno72.machine.vo;

import com.inno72.machine.model.Inno72Locale;

public class Inno72LocaleVo extends Inno72Locale{
	
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

    /**
     * 商圈ID
     */
    private String circle;
    
    private String areaName;
    
    private int userNum;
    
    
    

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

	
	
   
}