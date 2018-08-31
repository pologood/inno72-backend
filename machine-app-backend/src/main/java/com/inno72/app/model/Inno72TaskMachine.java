package com.inno72.app.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_task_machine")
public class Inno72TaskMachine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 任务id
	 */
	@Column(name = "task_id")
	private String taskId;

	/**
	 * 机器id
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 机器code
	 */
	@Column(name = "machine_code")
	private String machineCode;

	/**
	 * 任务状态：0位派发，1已派发，2派发失败
	 */
	@Column(name = "task_status")
	private Integer taskStatus;

	/**
	 * 发送类型
	 */
	@Column(name = "do_type")
	private Integer doType;

	/**
	 * 执行状态：0未执行，1成功，2失败
	 */
	@Column(name = "do_status")
	private Integer doStatus;

	/**
	 * 执行结果
	 */
	@Column(name = "do_msg")
	private String doMsg;

	/**
	 * 执行时间
	 */
	@Column(name = "do_time")
	private LocalDateTime doTime;

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
	 * 获取任务id
	 *
	 * @return task_id - 任务id
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * 设置任务id
	 *
	 * @param taskId
	 *            任务id
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 获取机器id
	 *
	 * @return machine_id - 机器id
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * 设置机器id
	 *
	 * @param machineId
	 *            机器id
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * 获取机器code
	 *
	 * @return machine_code - 机器code
	 */
	public String getMachineCode() {
		return machineCode;
	}

	/**
	 * 设置机器code
	 *
	 * @param machineCode
	 *            机器code
	 */
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	/**
	 * 获取任务状态：0位派发，1已派发，2派发失败
	 *
	 * @return task_status - 任务状态：0位派发，1已派发，2派发失败
	 */
	public Integer getTaskStatus() {
		return taskStatus;
	}

	/**
	 * 设置任务状态：0位派发，1已派发，2派发失败
	 *
	 * @param taskStatus
	 *            任务状态：0位派发，1已派发，2派发失败
	 */
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * 获取发送类型
	 *
	 * @return do_type - 发送类型
	 */
	public Integer getDoType() {
		return doType;
	}

	/**
	 * 设置发送类型
	 *
	 * @param doType
	 *            发送类型
	 */
	public void setDoType(Integer doType) {
		this.doType = doType;
	}

	/**
	 * 获取执行状态：0未执行，1成功，2失败
	 *
	 * @return do_status - 执行状态：0未执行，1成功，2失败
	 */
	public Integer getDoStatus() {
		return doStatus;
	}

	/**
	 * 设置执行状态：0未执行，1成功，2失败
	 *
	 * @param doStatus
	 *            执行状态：0未执行，1成功，2失败
	 */
	public void setDoStatus(Integer doStatus) {
		this.doStatus = doStatus;
	}

	/**
	 * 获取执行结果
	 *
	 * @return do_msg - 执行结果
	 */
	public String getDoMsg() {
		return doMsg;
	}

	/**
	 * 设置执行结果
	 *
	 * @param doMsg
	 *            执行结果
	 */
	public void setDoMsg(String doMsg) {
		this.doMsg = doMsg;
	}

	/**
	 * 获取执行时间
	 *
	 * @return do_time - 执行时间
	 */
	public LocalDateTime getDoTime() {
		return doTime;
	}

	/**
	 * 设置执行时间
	 *
	 * @param doTime
	 *            执行时间
	 */
	public void setDoTime(LocalDateTime doTime) {
		this.doTime = doTime;
	}
}