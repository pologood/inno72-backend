package com.inno72.project.vo;

import java.util.List;

import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.model.Inno72ActivityPlanGameResult;

public class Inno72ActivityPlanVo extends Inno72ActivityPlan{
	
    private List<Inno72MachineVo> machines;
    
    private List<Inno72ActivityPlanGameResult> goods;
    
    private List<Inno72CouponVo> coupons;
    
    private String activityName;
    
    private String merchantName;
    
    private String shopName;
    
    private String gameName;
    
    private String startTimeStr;
    
    private String endTimeStr;
    
    
	public List<Inno72MachineVo> getMachines() {
		return machines;
	}

	public void setMachines(List<Inno72MachineVo> machines) {
		this.machines = machines;
	}

	public List<Inno72ActivityPlanGameResult> getGoods() {
		return goods;
	}

	public void setGoods(List<Inno72ActivityPlanGameResult> goods) {
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
	

	
    
    
}