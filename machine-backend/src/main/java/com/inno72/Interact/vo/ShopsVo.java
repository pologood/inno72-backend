package com.inno72.Interact.vo;

import com.inno72.project.model.Inno72Shops;

public class ShopsVo extends Inno72Shops {

	private Integer isVip;

	private String merchantName;

	public Integer getIsVip() {
		return isVip;
	}

	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

}