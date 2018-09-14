package com.inno72.project.vo;

import com.inno72.project.model.Inno72ActivityPlanGameResult;

public class Inno72ActivityPlanGameResultVo extends Inno72ActivityPlanGameResult {

	private String shopsId;

	private String shopName;

	private Integer number;

	private Integer userDayNumber;

	public String getShopsId() {
		return shopsId;
	}

	public void setShopsId(String shopsId) {
		this.shopsId = shopsId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getUserDayNumber() {
		return userDayNumber;
	}

	public void setUserDayNumber(Integer userDayNumber) {
		this.userDayNumber = userDayNumber;
	}

}