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

@Table(name = "inno72_machine_enter")
public class Inno72MachineEnter {
	/**
	 * 商品ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 机器编号
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 机器编号
	 */
	@Column(name = "machine_code")
	private String machineCode;

	/**
	 * 0 alipay，1京东
	 */
	@Column(name = "enter_type")
	private String enterType;

	/**
	 * 入驻状态：0位入驻，1入驻
	 */
	@Column(name = "enter_status")
	private Integer enterStatus;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 获取商品ID
	 *
	 * @return id - 商品ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置商品ID
	 *
	 * @param id
	 *            商品ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取机器编号
	 *
	 * @return machine_id - 机器编号
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * 设置机器编号
	 *
	 * @param machineId
	 *            机器编号
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * 获取机器编号
	 *
	 * @return machine_code - 机器编号
	 */
	public String getMachineCode() {
		return machineCode;
	}

	/**
	 * 设置机器编号
	 *
	 * @param machineCode
	 *            机器编号
	 */
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	/**
	 * 获取0 alipay，1京东
	 *
	 * @return enter_type - 0 alipay，1京东
	 */
	public String getEnterType() {
		return enterType;
	}

	/**
	 * 设置0 alipay，1京东
	 *
	 * @param enterType
	 *            0 alipay，1京东
	 */
	public void setEnterType(String enterType) {
		this.enterType = enterType;
	}

	public Integer getEnterStatus() {
		return enterStatus;
	}

	public void setEnterStatus(Integer enterStatus) {
		this.enterStatus = enterStatus;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Inno72MachineEnter) {
			Inno72MachineEnter other = (Inno72MachineEnter) obj;
			if (machineId.equals(other.machineId) && enterType.equals(other.enterType))
				return true;
		}
		return false;
	}

}