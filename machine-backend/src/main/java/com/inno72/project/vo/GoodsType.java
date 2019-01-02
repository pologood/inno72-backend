package com.inno72.project.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import com.inno72.project.model.Inno72GoodsType;

public class GoodsType {

	private String code;

	private String name;

	private Integer level;

	private String updateId;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	private List<Inno72GoodsType> children;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public List<Inno72GoodsType> getChildren() {
		return children;
	}

	public void setChildren(List<Inno72GoodsType> children) {
		this.children = children;
	}

}