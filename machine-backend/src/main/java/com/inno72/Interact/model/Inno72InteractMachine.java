package com.inno72.Interact.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_interact_machine")
public class Inno72InteractMachine {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 活动ID
	 */
	@Column(name = "interact_id")
	private String interactId;

	/**
	 * 机器ID
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 机器code
	 */
	@Column(name = "machine_code")
	private String machineCode;

	/**
	 * 开始时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "query_start_time")
	private LocalDateTime queryStartTime;

	/**
	 * 结束时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "query_end_time")
	private LocalDateTime queryEndTime;

	/**
	 * 机器活动时间状态：0正常，1长期
	 */
	@Column(name = "state")
	private Integer state;

	/**
	 * 获取ID
	 *
	 * @return id - ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 *
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取活动ID
	 *
	 * @return interact_id - 活动ID
	 */
	public String getInteractId() {
		return interactId;
	}

	/**
	 * 设置活动ID
	 *
	 * @param interactId
	 *            活动ID
	 */
	public void setInteractId(String interactId) {
		this.interactId = interactId;
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

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public LocalDateTime getQueryStartTime() {
		return queryStartTime;
	}

	public void setQueryStartTime(LocalDateTime queryStartTime) {
		this.queryStartTime = queryStartTime;
	}

	public LocalDateTime getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(LocalDateTime queryEndTime) {
		this.queryEndTime = queryEndTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}