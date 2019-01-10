package com.inno72.store.vo;

import java.util.List;
import java.util.Map;

import com.inno72.store.model.Inno72StoreOrder;

public class StoreOrderVo extends Inno72StoreOrder {

	private List<Map<String, Object>> expressList;

	public List<Map<String, Object>> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Map<String, Object>> expressList) {
		this.expressList = expressList;
	}

}