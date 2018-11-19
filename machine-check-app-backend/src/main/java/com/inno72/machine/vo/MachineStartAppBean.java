package com.inno72.machine.vo;

public class MachineStartAppBean {
	private String appPackageName;
	private int startStatus;// 1启动 2杀死
	private int appType;// 1：监控 2：正常

	public String getAppPackageName() {
		return appPackageName;
	}

	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public int getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(int startStatus) {
		this.startStatus = startStatus;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

}
