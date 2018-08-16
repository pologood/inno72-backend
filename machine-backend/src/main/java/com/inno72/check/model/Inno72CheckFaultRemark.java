package com.inno72.check.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_check_fault_remark")
public class Inno72CheckFaultRemark {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 故障ID
	 */
	@Column(name = "fault_id")
	private String faultId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * 用户name
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 用户类型（1.巡检人员，2.运营人员）
	 */
	private Integer type;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 是否删除（0.正常，1.删除）
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 获取主键
	 *
	 * @return id - 主键
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置主键
	 *
	 * @param id
	 *            主键
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取故障ID
	 *
	 * @return fault_id - 故障ID
	 */
	public String getFaultId() {
		return faultId;
	}

	/**
	 * 设置故障ID
	 *
	 * @param faultId
	 *            故障ID
	 */
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}

	/**
	 * 获取用户ID
	 *
	 * @return user_id - 用户ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 *
	 * @param userId
	 *            用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取用户类型（1.巡检人员，2.运营人员）
	 *
	 * @return type - 用户类型（1.巡检人员，2.运营人员）
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置用户类型（1.巡检人员，2.运营人员）
	 *
	 * @param type
	 *            用户类型（1.巡检人员，2.运营人员）
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取备注
	 *
	 * @return remark - 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 *
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * 获取是否删除（0.正常，1.删除）
	 *
	 * @return is_delete - 是否删除（0.正常，1.删除）
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置是否删除（0.正常，1.删除）
	 *
	 * @param isDelete
	 *            是否删除（0.正常，1.删除）
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}