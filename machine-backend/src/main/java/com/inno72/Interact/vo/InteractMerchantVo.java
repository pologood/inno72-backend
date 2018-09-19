package com.inno72.Interact.vo;

import com.inno72.project.model.Inno72Merchant;

public class InteractMerchantVo extends Inno72Merchant {

	/**
	 * 互动ID
	 */
	private String interactId;

	private Integer type;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}