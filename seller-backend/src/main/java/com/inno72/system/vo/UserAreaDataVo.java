package com.inno72.system.vo;

import java.util.List;

import com.inno72.system.model.Inno72UserFunctionData;

public class UserAreaDataVo {

	private String userId;

	private List<Inno72UserFunctionData> columnList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Inno72UserFunctionData> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Inno72UserFunctionData> columnList) {
		this.columnList = columnList;
	}

}