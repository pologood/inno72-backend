package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

public class MachineTime {

	private String machineId;

	private List<Map<String, String>> planTime;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public List<Map<String, String>> getPlanTime() {
		return planTime;
	}

	public void setPlanTime(List<Map<String, String>> planTime) {
		this.planTime = planTime;
	}

}