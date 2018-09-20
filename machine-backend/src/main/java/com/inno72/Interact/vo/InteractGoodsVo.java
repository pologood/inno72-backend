package com.inno72.Interact.vo;

import com.inno72.project.model.Inno72Goods;

public class InteractGoodsVo extends Inno72Goods {

	/**
	 * 互动ID
	 */
	private String interactId;

	private Integer userDayNumber;

	private Integer type;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public Integer getUserDayNumber() {
		return userDayNumber;
	}

	public void setUserDayNumber(Integer userDayNumber) {
		this.userDayNumber = userDayNumber;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}