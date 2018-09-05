package com.inno72.machine.vo;

public class UpdateMachineVo {
	private String machineId;
	private String localeId;
	private Integer openStatus = 0;
	private String monitorStart;
	private String monitorEnd;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getLocaleId() {
		return localeId;
	}

	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	public String getMonitorStart() {
		return monitorStart;
	}

	public void setMonitorStart(String monitorStart) {
		this.monitorStart = monitorStart;
	}

	public String getMonitorEnd() {
		return monitorEnd;
	}

	public void setMonitorEnd(String monitorEnd) {
		this.monitorEnd = monitorEnd;
	}

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}

}
