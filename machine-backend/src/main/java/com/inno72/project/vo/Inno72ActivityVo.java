package com.inno72.project.vo;

import java.util.List;

import com.inno72.project.model.Inno72Activity;
import com.inno72.project.model.Inno72Shops;

public class Inno72ActivityVo extends Inno72Activity {

	private String state;

	private String prizeType;

	private String shopName;

	private String merchantName;

	private List<Inno72Shops> shops;

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

	public List<Inno72Shops> getShops() {
		return shops;
	}

	public void setShops(List<Inno72Shops> shops) {
		this.shops = shops;
	}

}