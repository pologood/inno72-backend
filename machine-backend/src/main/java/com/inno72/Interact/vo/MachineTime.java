package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

public class MachineTime {

	private String machineId;

	private String machineCode;

	private Integer state;

	private List<Map<String, String>> planTime;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<Map<String, String>> getPlanTime() {
		return planTime;
	}

	public void setPlanTime(List<Map<String, String>> planTime) {
		this.planTime = planTime;
	}

}