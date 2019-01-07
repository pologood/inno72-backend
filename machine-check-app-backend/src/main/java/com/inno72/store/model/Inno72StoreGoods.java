package com.inno72.store.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_store_goods")
public class Inno72StoreGoods {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 仓库ID
	 */
	@Column(name = "store_id")
	private String storeId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 商品数量
	 */
	private Integer number;

	/**
	 * 容量（方）
	 */
	private Integer capacity;

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

	@Transient
	private String goodsName;

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
	 * 获取仓库ID
	 *
	 * @return store_id - 仓库ID
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * 设置仓库ID
	 *
	 * @param storeId
	 *            仓库ID
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * 获取商品ID
	 *
	 * @return goods_id - 商品ID
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * 设置商品ID
	 *
	 * @param goodsId
	 *            商品ID
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
}