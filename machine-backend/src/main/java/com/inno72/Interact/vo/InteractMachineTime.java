package com.inno72.Interact.vo;

import java.util.List;

public class InteractMachineTime {

	private String interactId;

	private String queryStartTime;

	private String queryEndTime;

	private List<MachineTime> machines;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public String getQueryStartTime() {
		return queryStartTime;
	}

	public void setQueryStartTime(String queryStartTime) {
		this.queryStartTime = queryStartTime;
	}

	public String getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(String queryEndTime) {
		this.queryEndTime = queryEndTime;
	}

	public List<MachineTime> getMachines() {
		return machines;
	}

	public void setMachines(List<MachineTime> machines) {
		this.machines = machines;
	}

}