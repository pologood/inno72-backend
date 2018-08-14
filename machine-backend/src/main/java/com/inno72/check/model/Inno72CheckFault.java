package com.inno72.check.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
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
	@NotBlank(message = "请选择机器")
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 工单编号
	 */
	private String code;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 工单类型（1.故障，2.报警，3.补货，4.投诉）
	 */
	@NotNull(message = "请选择工单类型")
	@Column(name = "work_type")
	private Integer workType;

	/**
	 * 工单状态（1.待接单，2.处理中，3.已完成，4.已确认，5.已关闭）
	 */
	private Integer status;

	/**
	 * 来源(1.巡检上报，2.运营派单，3.报警派单)
	 */
	private Integer source;

	/**
	 * 紧急状态（1.日常，2.紧急）
	 */
	@NotNull(message = "请选择紧急状态")
	@Column(name = "urgent_status")
	private Integer urgentStatus;

	/**
	 * 故障类型
	 */
	private String type;

	/**
	 * 备注
	 */
	@NotBlank(message = "请填写工单描述")
	private String remark;

	/**
	 * 派单人
	 */
	@Column(name = "submit_user")
	private String submitUser;

	/**
	 * 派单人ID
	 */
	@Column(name = "submit_id")
	private String submitId;

	/**
	 * 派单人类型（1.巡检人员，2.运营人员）
	 */
	@Column(name = "submit_user_type")
	private Integer submitUserType;

	/**
	 * 接单人
	 */
	@Column(name = "receive_user")
	private String receiveUser;

	/**
	 * 接单人ID
	 */
	@Column(name = "receive_id")
	private String receiveId;

	/**
	 * 解决人
	 */
	@Column(name = "finish_user")
	private String finishUser;

	/**
	 * 解决人ID
	 */
	@Column(name = "finish_id")
	private String finishId;

	/**
	 * 提交时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "submit_time")
	private LocalDateTime submitTime;

	/**
	 * 接单时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "talking_time")
	private LocalDateTime talkingTime;

	/**
	 * 解决时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "finish_time")
	private LocalDateTime finishTime;

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

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
	 * 获取工单编号
	 *
	 * @return code - 工单编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置工单编号
	 *
	 * @param code
	 *            工单编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取标题
	 *
	 * @return title - 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取工单类型（1.故障，2.报警，3.补货，4.投诉）
	 *
	 * @return work_type - 工单类型（1.故障，2.报警，3.补货，4.投诉）
	 */
	public Integer getWorkType() {
		return workType;
	}

	/**
	 * 设置工单类型（1.故障，2.报警，3.补货，4.投诉）
	 *
	 * @param workType
	 *            工单类型（1.故障，2.报警，3.补货，4.投诉）
	 */
	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	/**
	 * 获取工单状态（1.待接单，2.处理中，3.已完成，4.已确认，5.已关闭）
	 *
	 * @return status - 工单状态（1.待接单，2.处理中，3.已完成，4.已确认，5.已关闭）
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置工单状态（1.待接单，2.处理中，3.已完成，4.已确认，5.已关闭）
	 *
	 * @param status
	 *            工单状态（1.待接单，2.处理中，3.已完成，4.已确认，5.已关闭）
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取来源(1.巡检上报，2.运营派单，3.报警派单)
	 *
	 * @return source - 来源(1.巡检上报，2.运营派单，3.报警派单)
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * 设置来源(1.巡检上报，2.运营派单，3.报警派单)
	 *
	 * @param source
	 *            来源(1.巡检上报，2.运营派单，3.报警派单)
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * 获取紧急状态（1.日常，2.紧急）
	 *
	 * @return urgent_status - 紧急状态（1.日常，2.紧急）
	 */
	public Integer getUrgentStatus() {
		return urgentStatus;
	}

	/**
	 * 设置紧急状态（1.日常，2.紧急）
	 *
	 * @param urgentStatus
	 *            紧急状态（1.日常，2.紧急）
	 */
	public void setUrgentStatus(Integer urgentStatus) {
		this.urgentStatus = urgentStatus;
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
	 * 获取派单人
	 *
	 * @return submit_user - 派单人
	 */
	public String getSubmitUser() {
		return submitUser;
	}

	/**
	 * 设置派单人
	 *
	 * @param submitUser
	 *            派单人
	 */
	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	/**
	 * 获取派单人ID
	 *
	 * @return submit_id - 派单人ID
	 */
	public String getSubmitId() {
		return submitId;
	}

	/**
	 * 设置派单人ID
	 *
	 * @param submitId
	 *            派单人ID
	 */
	public void setSubmitId(String submitId) {
		this.submitId = submitId;
	}

	/**
	 * 获取派单人类型（1.巡检人员，2.运营人员）
	 *
	 * @return submit_user_type - 派单人类型（1.巡检人员，2.运营人员）
	 */
	public Integer getSubmitUserType() {
		return submitUserType;
	}

	/**
	 * 设置派单人类型（1.巡检人员，2.运营人员）
	 *
	 * @param submitUserType
	 *            派单人类型（1.巡检人员，2.运营人员）
	 */
	public void setSubmitUserType(Integer submitUserType) {
		this.submitUserType = submitUserType;
	}

	/**
	 * 获取接单人
	 *
	 * @return receive_user - 接单人
	 */
	public String getReceiveUser() {
		return receiveUser;
	}

	/**
	 * 设置接单人
	 *
	 * @param receiveUser
	 *            接单人
	 */
	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	/**
	 * 获取接单人ID
	 *
	 * @return receive_id - 接单人ID
	 */
	public String getReceiveId() {
		return receiveId;
	}

	/**
	 * 设置接单人ID
	 *
	 * @param receiveId
	 *            接单人ID
	 */
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
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
	 * 获取解决人ID
	 *
	 * @return finish_id - 解决人ID
	 */
	public String getFinishId() {
		return finishId;
	}

	/**
	 * 设置解决人ID
	 *
	 * @param finishId
	 *            解决人ID
	 */
	public void setFinishId(String finishId) {
		this.finishId = finishId;
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
	 * 获取接单时间
	 *
	 * @return talking_time - 接单时间
	 */
	public LocalDateTime getTalkingTime() {
		return talkingTime;
	}

	/**
	 * 设置接单时间
	 *
	 * @param talkingTime
	 *            接单时间
	 */
	public void setTalkingTime(LocalDateTime talkingTime) {
		this.talkingTime = talkingTime;
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
	 * 获取修改时间
	 *
	 * @return update_time - 修改时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置修改时间
	 *
	 * @param updateTime
	 *            修改时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
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
}