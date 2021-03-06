package com.inno72.machine.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_supply_channel")
public class Inno72SupplyChannel {
	/**
	 * uuid
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 货道编号
	 */
	private String code;

	/**
	 * 货道名称
	 */
	private String name;

	/**
	 * 状态（0.未合并，1.已合并）
	 */
	private Integer status;

	/**
	 * 机器编号
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 父货道编号
	 */
	@Column(name = "parent_code")
	private String parentCode;

	/**
	 * 商品容量
	 */
	@Column(name = "volume_count")
	private Integer volumeCount;

	/**
	 * 商品数量
	 */
	@Column(name = "goods_count")
	private Integer goodsCount;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 修改人ID
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 是否删除（0.正常，1.删除）
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	@Column(name = "work_status")
	private Integer workStatus;

	/**
	 * 获取uuid
	 *
	 * @return id - uuid
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置uuid
	 *
	 * @param id
	 *            uuid
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取货道编号
	 *
	 * @return code - 货道编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置货道编号
	 *
	 * @param code
	 *            货道编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取货道名称
	 *
	 * @return name - 货道名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置货道名称
	 *
	 * @param name
	 *            货道名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取状态（0.未合并，1.已合并）
	 *
	 * @return status - 状态（0.未合并，1.已合并）
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置状态（0.未合并，1.已合并）
	 *
	 * @param status
	 *            状态（0.未合并，1.已合并）
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	 * 获取父货道编号
	 *
	 * @return parent_code - 父货道编号
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * 设置父货道编号
	 *
	 * @param parentCode
	 *            父货道编号
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * 获取商品容量
	 *
	 * @return volume_count - 商品容量
	 */
	public Integer getVolumeCount() {
		return volumeCount;
	}

	/**
	 * 设置商品容量
	 *
	 * @param volumeCount
	 *            商品容量
	 */
	public void setVolumeCount(Integer volumeCount) {
		this.volumeCount = volumeCount;
	}

	/**
	 * 获取商品数量
	 *
	 * @return goods_count - 商品数量
	 */
	public Integer getGoodsCount() {
		return goodsCount;
	}

	/**
	 * 设置商品数量
	 *
	 * @param goodsCount
	 *            商品数量
	 */
	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	/**
	 * 获取创建人ID
	 *
	 * @return create_id - 创建人ID
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人ID
	 *
	 * @param createId
	 *            创建人ID
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取修改人ID
	 *
	 * @return update_id - 修改人ID
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置修改人ID
	 *
	 * @param updateId
	 *            修改人ID
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
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

	/**
	 * @return work_status
	 */
	public Integer getWorkStatus() {
		return workStatus;
	}

	/**
	 * @param workStatus
	 */
	public void setWorkStatus(Integer workStatus) {
		this.workStatus = workStatus;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

}