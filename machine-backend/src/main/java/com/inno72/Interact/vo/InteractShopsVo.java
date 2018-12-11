package com.inno72.Interact.vo;

import java.util.List;

public class InteractShopsVo {

	/**
	 * 互动ID
	 */
	private String interactId;

	private Integer isVip;

	/**
	 * 添加的店铺
	 */
	private List<ShopsVo> shops;

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

	public List<ShopsVo> getShops() {
		return shops;
	}

	public void setShops(List<ShopsVo> shops) {
		this.shops = shops;
	}

}