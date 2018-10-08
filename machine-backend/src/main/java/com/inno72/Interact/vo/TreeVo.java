package com.inno72.Interact.vo;

import java.util.List;

public class TreeVo {

	private String id;

	private String name;

	private List<TreeVo> childList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeVo> getChildList() {
		return childList;
	}

	public void setChildList(List<TreeVo> childList) {
		this.childList = childList;
	}

}