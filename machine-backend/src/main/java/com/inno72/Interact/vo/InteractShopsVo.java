package com.inno72.Interact.vo;

import com.inno72.project.model.Inno72Shops;

public class InteractShopsVo extends Inno72Shops {

	/**
	 * 互动ID
	 */
	private String interactId;

	private Integer isVip;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public Integer getIsVip() {
		return isVip;
	}

	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

}