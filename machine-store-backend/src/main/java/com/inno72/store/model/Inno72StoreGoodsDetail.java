package com.inno72.store.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_store_goods_detail")
public class Inno72StoreGoodsDetail {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 仓库商品ID
	 */
	@Column(name = "store_goods_id")
	private String storeGoodsId;

	/**
	 * 类型：0入库，1出库
	 */
	private Integer type;

	/**
	 * 商品数量
	 */
	private Integer number;

	/**
	 * 容量（方）
	 */
	private Integer capacity;

	/**
	 * 描述
	 */
	private String detail;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private LocalDateTime updateTime;

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
	 * 获取仓库商品ID
	 *
	 * @return store_goods_id - 仓库商品ID
	 */
	public String getStoreGoodsId() {
		return storeGoodsId;
	}

	/**
	 * 设置仓库商品ID
	 *
	 * @param storeGoodsId
	 *            仓库商品ID
	 */
	public void setStoreGoodsId(String storeGoodsId) {
		this.storeGoodsId = storeGoodsId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取商品数量
	 *
	 * @return number - 商品数量
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * 设置商品数量
	 *
	 * @param number
	 *            商品数量
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * 获取容量（方）
	 *
	 * @return capacity - 容量（方）
	 */
	public Integer getCapacity() {
		return capacity;
	}

	/**
	 * 设置容量（方）
	 *
	 * @param capacity
	 *            容量（方）
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	/**
	 * 获取描述
	 *
	 * @return detail - 描述
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * 设置描述
	 *
	 * @param detail
	 *            描述
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
}