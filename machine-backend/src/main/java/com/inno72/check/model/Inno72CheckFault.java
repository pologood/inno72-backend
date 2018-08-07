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

@Table(name = "inno72_check_fault")
public class Inno72CheckFault {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 机器ID
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 故障类型
	 */
	private String type;

	/**
	 * 工单编号
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 提交人
	 */
	@Column(name = "submit_user")
	private String submitUser;

	/**
	 * 解决人
	 */
	@Column(name = "finish_user")
	private String finishUser;

	/**
	 * 提交时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "submit_time")
	private LocalDateTime submitTime;

	/**
	 * 解决时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "finish_time")
	private LocalDateTime finishTime;

	/**
	 * 状态（0.未解决，1.已解决，默认0）
	 */
	private Integer status;

	/**
	 * 解决方案
	 */
	@Column(name = "finish_remark")
	private String finishRemark;

	/**
	 * 提醒状态（0.未提醒，1.已提醒，默认0）
	 */
	@Column(name = "remind_status")
	private Integer remindStatus;

	/**
	 * 备注
	 */
	private String remark;

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
	 * 获取机器ID
	 *
	 * @return machine_id - 机器ID
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * 设置机器ID
	 *
	 * @param machineId
	 *            机器ID
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * 获取故障类型
	 *
	 * @return type - 故障类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置故障类型
	 *
	 * @param type
	 *            故障类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取提交人
	 *
	 * @return submit_user - 提交人
	 */
	public String getSubmitUser() {
		return submitUser;
	}

	/**
	 * 设置提交人
	 *
	 * @param submitUser
	 *            提交人
	 */
	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	/**
	 * 获取解决人
	 *
	 * @return finish_user - 解决人
	 */
	public String getFinishUser() {
		return finishUser;
	}

	/**
	 * 设置解决人
	 *
	 * @param finishUser
	 *            解决人
	 */
	public void setFinishUser(String finishUser) {
		this.finishUser = finishUser;
	}

	/**
	 * 获取提交时间
	 *
	 * @return submit_time - 提交时间
	 */
	public LocalDateTime getSubmitTime() {
		return submitTime;
	}

	/**
	 * 设置提交时间
	 *
	 * @param submitTime
	 *            提交时间
	 */
	public void setSubmitTime(LocalDateTime submitTime) {
		this.submitTime = submitTime;
	}

	/**
	 * 获取解决时间
	 *
	 * @return finish_time - 解决时间
	 */
	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	/**
	 * 设置解决时间
	 *
	 * @param finishTime
	 *            解决时间
	 */
	public void setFinishTime(LocalDateTime finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * 获取状态（0.未解决，1.已解决，默认0）
	 *
	 * @return status - 状态（0.未解决，1.已解决，默认0）
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置状态（0.未解决，1.已解决，默认0）
	 *
	 * @param status
	 *            状态（0.未解决，1.已解决，默认0）
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取解决方案
	 *
	 * @return finish_remark - 解决方案
	 */
	public String getFinishRemark() {
		return finishRemark;
	}

	/**
	 * 设置解决方案
	 *
	 * @param finishRemark
	 *            解决方案
	 */
	public void setFinishRemark(String finishRemark) {
		this.finishRemark = finishRemark;
	}

	/**
	 * 获取提醒状态（0.未提醒，1.已提醒，默认0）
	 *
	 * @return remind_status - 提醒状态（0.未提醒，1.已提醒，默认0）
	 */
	public Integer getRemindStatus() {
		return remindStatus;
	}

	/**
	 * 设置提醒状态（0.未提醒，1.已提醒，默认0）
	 *
	 * @param remindStatus
	 *            提醒状态（0.未提醒，1.已提醒，默认0）
	 */
	public void setRemindStatus(Integer remindStatus) {
		this.remindStatus = remindStatus;
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
}