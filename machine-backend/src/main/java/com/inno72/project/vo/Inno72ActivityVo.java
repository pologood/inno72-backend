package com.inno72.project.vo;

import java.util.List;
import java.util.Map;

import com.inno72.project.model.Inno72Activity;

public class Inno72ActivityVo extends Inno72Activity {

	private String state;

	private String prizeType;

	private String shopName;

	private String merchantName;

	private List<Inno72ShopsVo> shops;

	private List<Map<String, Object>> planTime;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
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

	public List<Inno72ShopsVo> getShops() {
		return shops;
	}

	public void setShops(List<Inno72ShopsVo> shops) {
		this.shops = shops;
	}

	public List<Map<String, Object>> getPlanTime() {
		return planTime;
	}

	public void setPlanTime(List<Map<String, Object>> planTime) {
		this.planTime = planTime;
	}

}