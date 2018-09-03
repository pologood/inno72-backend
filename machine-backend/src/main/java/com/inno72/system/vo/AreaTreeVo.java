package com.inno72.system.vo;

import java.util.List;

public class AreaTreeVo {

	private String name;

	private String code;

	private List<AreaTreeVo> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<AreaTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<AreaTreeVo> children) {
		this.children = children;
	}

}