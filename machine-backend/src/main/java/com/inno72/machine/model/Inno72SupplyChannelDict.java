package com.inno72.machine.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_supply_channel_dict")
public class Inno72SupplyChannelDict {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 编号
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 是否删除（0.正常，1.已删除）
	 */
	private int isDelete;

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
	 * 获取编号
	 *
	 * @return code - 编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置编号
	 *
	 * @param code
	 *            编号
	 */
	public void setCode(String code) {
		this.code = code;
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

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
}