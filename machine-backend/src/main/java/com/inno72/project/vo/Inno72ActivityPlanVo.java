package com.inno72.project.vo;

import java.util.List;

import com.inno72.project.model.Inno72ActivityPlan;

public class Inno72ActivityPlanVo extends Inno72ActivityPlan {

	private List<Inno72MachineVo> machines;

	private List<Inno72ActivityPlanGameResultVo> goods;

	private List<Inno72CouponVo> coupons;

	private String activityName;

	private String activityType;

	private String merchantName;

	private String shopName;

	private String shopId;

	private String gameName;

	private Integer maxGoodsNum;

	private String startTimeStr;

	private String endTimeStr;

	public List<Inno72MachineVo> getMachines() {
		return machines;
	}

	public void setMachines(List<Inno72MachineVo> machines) {
		this.machines = machines;
	}

	public List<Inno72ActivityPlanGameResultVo> getGoods() {
		return goods;
	}

	public void setGoods(List<Inno72ActivityPlanGameResultVo> goods) {
		this.goods = goods;
	}

	public List<Inno72CouponVo> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Inno72CouponVo> coupons) {
		this.coupons = coupons;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Integer getMaxGoodsNum() {
		return maxGoodsNum;
	}

	public void setMaxGoodsNum(Integer maxGoodsNum) {
		this.maxGoodsNum = maxGoodsNum;
	}

}