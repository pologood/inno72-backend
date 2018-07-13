package com.inno72.project.vo;


import com.inno72.project.model.Inno72Coupon;

public class Inno72CouponVo extends Inno72Coupon{
	private int  resultCode;
	
	private String  resultRemark;
	
	private String  prizeType;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultRemark() {
		return resultRemark;
	}

	public void setResultRemark(String resultRemark) {
		this.resultRemark = resultRemark;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}
	
	
	
	
    
}