package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

import com.inno72.project.model.Inno72Goods;

public class InteractGoodsVo extends Inno72Goods {

	/**
	 * 互动ID
	 */
	private String interactId;

	private Integer userDayNumber;

	private Integer type;

	private String shopName;
	private String merchantName;

	private Integer isAlone;

	private List<Map<String, String>> goodsList;

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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getIsAlone() {
		return isAlone;
	}

	public void setIsAlone(Integer isAlone) {
		this.isAlone = isAlone;
	}

	public List<Map<String, String>> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Map<String, String>> goodsList) {
		this.goodsList = goodsList;
	}

}