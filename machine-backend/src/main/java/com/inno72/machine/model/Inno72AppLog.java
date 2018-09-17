package com.inno72.machine.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_app_log")
public class Inno72AppLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 1 系统日志 2 业务日志 3 产品日志
	 */
	@Column(name = "log_type")
	private Integer logType;

	@Column(name = "error_log")
	private String errorLog;

	/**
	 * 1 已收到 2 已解析
	 */
	private Integer status;

	@Column(name = "recive_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime reciveTime;

	@Column(name = "process_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime processTime;

	@Column(name = "log_url")
	private String logUrl;

	@Column(name = "machine_code")
	private String machineCode;

	@Column(name = "start_time")
	private String startTime;

	@Column(name = "end_time")
	private String endTime;

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
	 * 获取1 系统日志 2 业务日志 3 产品日志
	 *
	 * @return log_type - 1 系统日志 2 业务日志 3 产品日志
	 */
	public Integer getLogType() {
		return logType;
	}

	/**
	 * 设置1 系统日志 2 业务日志 3 产品日志
	 *
	 * @param logType
	 *            1 系统日志 2 业务日志 3 产品日志
	 */
	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	/**
	 * @return error_log
	 */
	public String getErrorLog() {
		return errorLog;
	}

	/**
	 * @param errorLog
	 */
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	/**
	 * 获取1 已收到 2 已解析
	 *
	 * @return status - 1 已收到 2 已解析
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置1 已收到 2 已解析
	 *
	 * @param status
	 *            1 已收到 2 已解析
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return recive_time
	 */
	public LocalDateTime getReciveTime() {
		return reciveTime;
	}

	/**
	 * @param reciveTime
	 */
	public void setReciveTime(LocalDateTime reciveTime) {
		this.reciveTime = reciveTime;
	}

	/**
	 * @return process_time
	 */
	public LocalDateTime getProcessTime() {
		return processTime;
	}

	/**
	 * @param processTime
	 */
	public void setProcessTime(LocalDateTime processTime) {
		this.processTime = processTime;
	}

	/**
	 * @return log_url
	 */
	public String getLogUrl() {
		return logUrl;
	}

	/**
	 * @param logUrl
	 */
	public void setLogUrl(String logUrl) {
		this.logUrl = logUrl;
	}

	/**
	 * @return machine_code
	 */
	public String getMachineCode() {
		return machineCode;
	}

	/**
	 * @param machineCode
	 */
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	/**
	 * @return start_time
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return end_time
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}