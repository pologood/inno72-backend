package com.inno72.model;

import java.util.Date;
import java.util.List;

public class MachineAppStatus {
	private String machineId;
	private List<AppStatus> status;
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public List<AppStatus> getStatus() {
		return status;
	}

	public void setStatus(List<AppStatus> status) {
		this.status = status;
	}

}
