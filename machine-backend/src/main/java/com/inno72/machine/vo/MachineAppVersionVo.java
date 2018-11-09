package com.inno72.machine.vo;

import java.util.List;

public class MachineAppVersionVo {
	private String machineCode;
	private String createTime;
	private List<String> appInfo;

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<String> getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(List<String> appInfo) {
		this.appInfo = appInfo;
	}

}
