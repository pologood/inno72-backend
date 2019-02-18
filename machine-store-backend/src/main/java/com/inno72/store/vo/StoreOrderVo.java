package com.inno72.store.vo;

import java.util.List;

import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.model.Inno72StoreOrder;

public class StoreOrderVo extends Inno72StoreOrder {

	private String goodsName;

	private String goodsCode;

	private String merchantName;

	private List<Inno72StoreExpress> expressList;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<Inno72StoreExpress> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Inno72StoreExpress> expressList) {
		this.expressList = expressList;
	}

}