package com.inno72.project.vo;

import java.util.List;

import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.model.Inno72ActivityPlanGameResult;

public class Inno72ActivityPlanVo extends Inno72ActivityPlan{
	
    private List<Inno72MachineVo> machines;
    
    private List<Inno72ActivityPlanGameResult> goods;
    
    private List<Inno72CouponVo> coupons;
    
    private String activityName;
    
    
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
	
    

	
    
    
}