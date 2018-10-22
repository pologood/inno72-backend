package com.inno72.Interact.vo;

import com.inno72.Interact.model.Inno72InteractMachineGoods;

public class Inno72InteractMachineGoodsVo extends Inno72InteractMachineGoods {

	private String goodsName;

	private String startTimeStr;

	private String endTimeStr;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

}