package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

import com.inno72.Interact.model.Inno72Interact;

public class Inno72InteractVo extends Inno72Interact {

	private Integer type;

	private String gameName;

	private List<Map<String, String>> enterTypeList;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public List<Map<String, String>> getEnterTypeList() {
		return enterTypeList;
	}

	public void setEnterTypeList(List<Map<String, String>> enterTypeList) {
		this.enterTypeList = enterTypeList;
	}

}