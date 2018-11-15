package com.inno72.Interact.vo;

import java.util.List;

public class InteractMerchantVo {

	/**
	 * 互动ID
	 */
	private String interactId;

	/**
	 * 添加的商户
	 */
	private List<Merchant> merchants;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public List<Merchant> getMerchants() {
		return merchants;
	}

	public void setMerchants(List<Merchant> merchants) {
		this.merchants = merchants;
	}

}