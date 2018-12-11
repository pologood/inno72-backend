package com.inno72.project.vo;

import com.inno72.project.model.Inno72Shops;

import lombok.Data;

@Data
public class Inno72ShopsVo extends Inno72Shops {

	private Integer isVip;

	private String sessionKey;

	private String channelName;

	private String channelId;

	private String status;

	private String merchantAccountId;

	private String merchantName;

}