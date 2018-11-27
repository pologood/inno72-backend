package com.inno72.machine.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_machine_locale_detail")
public class Inno72MachineLocaleDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 机器id
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 当前点位ID
	 */
	@Column(name = "locale")
	private String locale;

	/**
	 * 改前点位ID
	 */
	@Column(name = "old_locale")
	private String oldLocale;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

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
	 * 获取当前点位ID
	 *
	 * @return locale_id - 当前点位ID
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * 设置当前点位ID
	 *
	 * @param localeId
	 *            当前点位ID
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * 获取改前点位ID
	 *
	 * @return old_locale - 改前点位ID
	 */
	public String getOldLocale() {
		return oldLocale;
	}

	/**
	 * 设置改前点位ID
	 *
	 * @param oldLocale
	 *            改前点位ID
	 */
	public void setOldLocale(String oldLocale) {
		this.oldLocale = oldLocale;
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
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}