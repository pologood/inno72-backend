package com.inno72.project.vo;

import com.inno72.project.model.Inno72Goods;

public class Inno72GoodsVo extends Inno72Goods {
	private String shopName;
	private String merchantName;

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

}