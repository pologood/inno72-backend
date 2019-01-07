package com.inno72.store.vo;

import javax.persistence.Column;

public class StoreOrderVo {

	/**
	 * 收货方
	 */
	private String receiver;

	/**
	 * 收货方ID
	 */
	private String receiveId;

	/**
	 * 商品
	 */
	private String goods;


	/**
	 * 商品数量
	 */
	private Integer number;


	/**
	 * 物流公司
	 */
	private String expressCompany;


	/**
	 * 物流单号
	 */
	private String expressNum;

	private String keyword;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
