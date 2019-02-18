package com.inno72.check.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Inno72CheckGoodsNum {

	/**
	 * uuid
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name="check_user_id")
	private String checkUserId;

	@Column(name="goods_id")
	private String goodsId;

	@Column(name="activity_id")
	private String activityId;

	@Column(name="receive_total_count")
	private int receiveTotalCount;

	@Column(name="supply_total_count")
	private int supplyTotalCount;

	@Column(name="differ_total_count")
	private int differTotalCount;

	@Transient
	private String activityName;

	@Transient
	private String goodsName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public int getReceiveTotalCount() {
		return receiveTotalCount;
	}

	public void setReceiveTotalCount(int receiveTotalCount) {
		this.receiveTotalCount = receiveTotalCount;
	}

	public int getSupplyTotalCount() {
		return supplyTotalCount;
	}

	public void setSupplyTotalCount(int supplyTotalCount) {
		this.supplyTotalCount = supplyTotalCount;
	}

	public int getDifferTotalCount() {
		return differTotalCount;
	}

	public void setDifferTotalCount(int differTotalCount) {
		this.differTotalCount = differTotalCount;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
}
