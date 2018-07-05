package com.inno72.machine.vo;

public class SupplyChannelVo {

	/**
	 * 机器ID
	 */
	public String merchantId;

	/**
	 * 被合并货道code
	 */
	private String fromCode;

	/**
	 * 主货道code
	 */
	private String toCode;

	/**
	 * 商品ID数组
	 */
	private String[] goodsCodes;

	/**
	 * 货道code
	 */
	private String code;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getToCode() {
		return toCode;
	}

	public void setToCode(String toCode) {
		this.toCode = toCode;
	}

	public String[] getGoodsCodes() {
		return goodsCodes;
	}

	public void setGoodsCodes(String[] goodsCodes) {
		this.goodsCodes = goodsCodes;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
