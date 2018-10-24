package com.inno72.check.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_check_fault_type")
public class Inno72CheckFaultType {
	/**
	 * Code
	 */
	@Id
	@Column(name = "code")
	private String code;

	/**
	 * 父级code
	 */
	@Column(name = "parent_code")
	private String parentCode;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 父级名称
	 */
	@Column(name = "parent_name")
	private String parentName;

	/**
	 * 级别
	 */
	@Column(name = "level")
	private Integer level;

	/**
	 * 上报方式:0非直接上报,1直接上报
	 */
	@Column(name = "submit_type")
	private Integer submitType;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 获取Code
	 *
	 * @return code - Code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置Code
	 *
	 * @param code
	 *            Code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取父级code
	 *
	 * @return parent_code - 父级code
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * 设置父级code
	 *
	 * @param parentCode
	 *            父级code
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
	 * 获取父级名称
	 *
	 * @return parent_name - 父级名称
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * 设置父级名称
	 *
	 * @param parentName
	 *            父级名称
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * 获取级别
	 *
	 * @return level - 级别
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * 设置级别
	 *
	 * @param level
	 *            级别
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSubmitType() {
		return submitType;
	}

	public void setSubmitType(Integer submitType) {
		this.submitType = submitType;
	}

	/**
	 * 获取创建人
	 *
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId
	 *            创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
}