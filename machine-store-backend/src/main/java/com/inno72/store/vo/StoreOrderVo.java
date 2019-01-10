package com.inno72.store.vo;

import java.util.List;

import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.model.Inno72StoreOrder;

public class StoreOrderVo extends Inno72StoreOrder {

	private List<Inno72StoreExpress> expressList;

	public List<Inno72StoreExpress> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Inno72StoreExpress> expressList) {
		this.expressList = expressList;
	}

}