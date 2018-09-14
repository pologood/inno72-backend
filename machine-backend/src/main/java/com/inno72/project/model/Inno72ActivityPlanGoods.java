package com.inno72.project.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_activity_plan_goods")
public class Inno72ActivityPlanGoods {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;

	/**
	 * 活动排期ID
	 */
	@Column(name = "activity_plan_id")
	private String activityPlanId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 商品数量
	 */
	@Column(name = "number")
	private Integer number;

	/**
	 * 商品每天获取数量
	 */
	@Column(name = "user_day_number")
	private Integer userDayNumber;

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
	 * 获取活动排期ID
	 *
	 * @return activity_plan_id - 活动排期ID
	 */
	public String getActivityPlanId() {
		return activityPlanId;
	}

	/**
	 * 设置活动排期ID
	 *
	 * @param activityPlanId
	 *            活动排期ID
	 */
	public void setActivityPlanId(String activityPlanId) {
		this.activityPlanId = activityPlanId;
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getUserDayNumber() {
		return userDayNumber;
	}

	public void setUserDayNumber(Integer userDayNumber) {
		this.userDayNumber = userDayNumber;
	}

}