package com.inno72.project.vo;

import com.inno72.project.model.Inno72Shops;

public class Inno72ShopsVo extends Inno72Shops {

	private Integer isVip;

	private String sessionKey;

	public Integer getIsVip() {
		return isVip;
	}

	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

}