package com.inno72.project.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_dictionary")
public class Inno72Dictionary {
	@Id
	private String id;

	/**
	 * 类型代码
	 */
	private String code;
	public static final String INDUSTRY = "001";
	public static final String CHANNEL = "002";
	public static final String ENTER = "003";

	/**
	 * 上级代码
	 */
	@Column(name = "parent_code")
	private String parentCode;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述字段
	 */
	private String description;

	/**
	 * 0：不可用， 1：正常
	 */
	private String status;

	/**
	 * 级别 1，2，3依次
	 */
	private Integer level;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取类型代码
	 *
	 * @return code - 类型代码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置类型代码
	 *
	 * @param code
	 *            类型代码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取上级代码
	 *
	 * @return parent_code - 上级代码
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * 设置上级代码
	 *
	 * @param parentCode
	 *            上级代码
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * 获取名称
	 *
	 * @return name - 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 *
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取描述字段
	 *
	 * @return description - 描述字段
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述字段
	 *
	 * @param description
	 *            描述字段
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取0：不可用， 1：正常
	 *
	 * @return status - 0：不可用， 1：正常
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置0：不可用， 1：正常
	 *
	 * @param status
	 *            0：不可用， 1：正常
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取级别 1，2，3依次
	 *
	 * @return level - 级别 1，2，3依次
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * 设置级别 1，2，3依次
	 *
	 * @param level
	 *            级别 1，2，3依次
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
}