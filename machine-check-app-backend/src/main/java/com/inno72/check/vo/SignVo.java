package com.inno72.check.vo;

public class SignVo {

	private String checkUserId;

	private String machineId;

	private String signDateStr;

	private String signTimeStr;

	private int type;

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getSignDateStr() {
		return signDateStr;
	}

	public void setSignDateStr(String signDateStr) {
		this.signDateStr = signDateStr;
	}

	public String getSignTimeStr() {
		return signTimeStr;
	}

	public void setSignTimeStr(String signTimeStr) {
		this.signTimeStr = signTimeStr;
	}

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
}
